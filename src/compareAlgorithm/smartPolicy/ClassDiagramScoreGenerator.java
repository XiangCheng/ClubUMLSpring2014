package compareAlgorithm.smartPolicy;

import java.io.IOException;
import java.util.Properties;

import controller.diagramparser.ClassDiagramParser;
import controller.diagramparser.DiagramParser;
import domain.Attributes;
import domain.CD_Class;
import domain.Classes;
import domain.DiagramPolicyScore;
import domain.MatricsObject;
import domain.MatricsType;
import domain.Policy;


public class ClassDiagramScoreGenerator {

	private Policy policy;
	private ClassDiagramParser diagramParser;
	private int classScore;
	private int attributesScore;
	private int assicoationScore;
	private int multiplicityScore;
	private int finalScore;
	private DiagramPolicyScore policyScore;
	private String justification;
	
	//Class weights/points
	private int totalClassBetweenPoints;
	private int totalClassMaxPoints;
	private int totalClassOverMaxPoints;
	private int totalClassUnderPoints;
	private int totalClassMinPoints;
	
	//Attribute weights/points
	private int avgAttributesBetweenPoints;
	private int avgAttributesMaxPoints;
	private int avgAttributesOverMaxPoints;
	private int avgAttributesUnderMinPoints;
	private int avgAttributesMinPoints;
	
	protected ClassDiagramScoreGenerator(Policy policyApplied, ClassDiagramParser parser) throws IOException
	{
		policy = policyApplied;
		diagramParser = parser;
		policyScore.setDiagramID(diagramParser.getDiagram().getDiagramId());
		policyScore.setPolicyID(policy.getPolicyID());
		initPoints();
					
	}
	
	//initialize by reading from a properties file, the values of the various weights assoiated
	private void initPoints() throws IOException
	{
		Properties prop = new Properties();
		prop.load(ClassDiagramScoreGenerator.class.getClassLoader().getResourceAsStream("clubUMLProperties.properties"));	 
		
		totalClassBetweenPoints = Integer.parseInt(prop.getProperty("CLASSBETWEENPOINTS")); 
		totalClassMaxPoints = Integer.parseInt(prop.getProperty("CLASSMAXPOINTS"));
		totalClassUnderPoints = Integer.parseInt(prop.getProperty("CLASSUNDERPOINTS"));
		totalClassOverMaxPoints = Integer.parseInt(prop.getProperty("CLASSOVERMAXPOINTS"));
		totalClassMinPoints = Integer.parseInt(prop.getProperty("CLASSMINPOINTS"));
		
		avgAttributesBetweenPoints = Integer.parseInt(prop.getProperty("ATTRIBUTEBETWEENPOINTS"));
		avgAttributesMaxPoints = Integer.parseInt(prop.getProperty("ATTRIBUTEMAXPOINTS"));
		avgAttributesOverMaxPoints = Integer.parseInt(prop.getProperty("ATTRIBUTEUNDERPOINTS"));
		avgAttributesUnderMinPoints = Integer.parseInt(prop.getProperty("ATTRIBUTEOVERMAXPOINTS"));
		avgAttributesMinPoints = Integer.parseInt(prop.getProperty("ATTRIBUTEMINPOINTS"));
	}
	
	public DiagramPolicyScore generateScore() throws Exception{
		
		for(MatricsObject mObj : policy.getMatricsObjects())
		{
			policyScore.setPolicyScore(GenerateMatricScore(diagramParser, mObj));
			policyScore.setJustification(justification);
		}
		return policyScore;
	}
	
	private int GenerateMatricScore(DiagramParser diagramParser, MatricsObject mObj)throws Exception{
		
		switch (mObj.getMatricsType()){
		
			case CLASSES:
				classScore = scoreClasses(mObj);
			case ATTRIBUTES:
				attributesScore = scoreAttributes(mObj);
			case MULTIPLICITIES:
				multiplicityScore = scoreMultiplicity(mObj);
			case ASSOCIATIONS:
				assicoationScore = scoreAssociations(mObj);
			case NOTDEFINED:
		
		}
		
		
		return aggregateScores();
	}
	
	public int aggregateScores(){
		
		finalScore = classScore + attributesScore + multiplicityScore + assicoationScore; //TO DO - Add weights
		//Logic to total Score
		return finalScore;
		
	}
	
	private int scoreClasses(MatricsObject mObj) throws Exception {
		int score = 0;
		if(mObj.getMatricsType() != MatricsType.CLASSES)
			throw new Exception("Wrong Matrics Type");
		int totalNoOfClasses = diagramParser.getClasses().size();
		int idealNoOfClasses = ((Classes)mObj).getIdealNoOfClasses();
		int maxNoOfClasses = ((Classes)mObj).getMaxNoOfClasses();
		int minNoOfClasses = ((Classes)mObj).getMinNoOfClasses();
		
//		 If the (A) total number of Classes > idealNoOfClasses < maxNoOfClasses 
//	      OR (B) minNoOfClasses < total number of Classes < idealNoOfClasses, then 
//	      totalClassesScore is 
//	      Absolute Value (idealNoOfClasses - total number of Classes ) * totalClassBetweenPoints
		
		if((totalNoOfClasses > idealNoOfClasses && totalNoOfClasses < maxNoOfClasses) || 
				(totalNoOfClasses < idealNoOfClasses && totalNoOfClasses > minNoOfClasses))
		{
			score = Math.abs((idealNoOfClasses - totalNoOfClasses)) * totalClassBetweenPoints;
		}
		else if(diagramParser.getClasses().size() == ((Classes)mObj).getIdealNoOfClasses())
		{
			score = 0;
		}
		else //overflow
		{
			if(totalNoOfClasses >= maxNoOfClasses){
				score = (totalClassMaxPoints + ((totalNoOfClasses - maxNoOfClasses) * totalClassOverMaxPoints));
			}
			else if(totalNoOfClasses <= minNoOfClasses){
				score = totalClassMinPoints + ((minNoOfClasses - totalNoOfClasses) * totalClassUnderPoints);
			}
			else
				throw new Exception("The ideal value is not in between Max and min values");
			
		}
		return score;
	}
	
	private int scoreAttributes(MatricsObject mObj) throws Exception{
		int score = 0;
		int avgNoAttribute = 0;
		int totalNoAttributes = 0;
		int idealNoOfAttribuutes = ((Attributes)mObj).getIdealNoOfAttributes();
		int maxNoAttributes = ((Attributes)mObj).getMaxNoOfAttributes();
		int minNoAttributes = ((Attributes)mObj).getMinNoOfAttributes();
		
		int totalNoOfClasses = diagramParser.getClasses().size();
		
		for(CD_Class cdClass : diagramParser.getClasses()){
			totalNoAttributes =+ cdClass.getAttributes().size();
		}
		
		if(totalNoOfClasses > 0)
			avgNoAttribute = totalNoAttributes/totalNoOfClasses;
		
		if(avgNoAttribute == idealNoOfAttribuutes){
			score = 0;
		}
		else if(((avgNoAttribute > idealNoOfAttribuutes) && (avgNoAttribute < maxNoAttributes)) ||
				((avgNoAttribute < idealNoOfAttribuutes) && (avgNoAttribute > minNoAttributes))){
			score = Math.abs((idealNoOfAttribuutes - avgNoAttribute)) * avgAttributesBetweenPoints;
		}
		else //overflow
		{
			if(totalNoOfClasses >= maxNoAttributes){
				score = (avgAttributesMaxPoints  + ((avgNoAttribute - maxNoAttributes) * avgAttributesOverMaxPoints ));
			}
			else if(totalNoOfClasses <= minNoAttributes){
				score = (avgAttributesMinPoints + ((minNoAttributes - avgNoAttribute) * avgAttributesUnderMinPoints));
				//score = ((minNoAttributes - totalNoOfClasses) * totalClassUnderPoints);
			}
			else
				throw new Exception("The ideal value is not in between Max and min values");
			
		}	
		
		return score;
	}
	
	private int scoreMultiplicity(MatricsObject mObj){
		int score = 0;
		//Code here
		return score;
	}
	
	private int scoreAssociations(MatricsObject mObj){
		int score = 0;
		//Code here
		return score;
	}
}
