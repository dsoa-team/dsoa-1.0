package br.ufpe.cin.dsoa.broker.norm;

//import java.sql.SQLException;
//import java.util.ArrayList;
import java.util.List;

//import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import br.ufpe.cin.dsoa.qos.Slo;

public class Normalizer {
	
	private double[][] matrix;
	
	private List<Slo> slos;
	private ServiceReference[] references;
	
	public Normalizer() {}
	
	public Normalizer(List<Slo> slos, ServiceReference... references) {
		super();
		this.slos = slos;
		this.references = references;
		this.matrix = mountMatrix(slos, references);
	}
	
	double[] min = min(slos, references, matrix);
	double[] max = max(slos, references, matrix);
	

	public double[][] normalizedMatrix(List<Slo> slos, ServiceReference[] references) {
		double[][] normalized =  new double[references.length][slos.size()];
		//double[][] matrix = mountMatrix(slos, references);
		
		//min = min(slos, references, matrix);
		//max = max(slos, references, matrix);
		
		for(int i=0; i<references.length; i++) {
			for(int j=0; j<slos.size(); j++) {
				String direction = slos.get(j).getExpression().getOperator();
				
				if(direction.equals("<=")) {
					if((max[j]-min[j]) == 0) {
						normalized[i][j] = 1;
					} else {
						normalized[i][j] = (max[j]-matrix[i][j])/(max[j]-min[j]);
					}
					
				} else {
					if(direction.equals(">=")) {
						if((max[j]-min[j]) == 0) {
							normalized[i][j] = 1;
						} else {
							normalized[i][j] = (matrix[i][j]-min[j])/(max[j]-min[j]);
						}
						
					} else {
						normalized[i][j] = 1;
					}
				}
				
			}  
		}
		return normalized;
	}

	private double[][] mountMatrix(List<Slo> slos,
			ServiceReference[] references) {
		double matrix[][] =  new double[references.length][slos.size()];
		//double dbmatrix[][] = new double[references.length][slos.size()];
		
		for(int i=0; i<references.length; i++) {
			
			for(int j=0; j<slos.size(); j++) {
				
				if(slos.get(j).getOperation() != null) {
					
					/*
					try {
						if(joint.existService(Double.parseDouble((references[i].getProperty(Constants.SERVICE_ID)).toString()))) {
							
							String op = slos.get(j).getExpression().getOperator();
							
							double v = joint.getValueAttributeOperationService(Double.parseDouble((references[i].getProperty(Constants.SERVICE_ID)).toString()), 
									slos.get(j).getOperation(), slos.get(j).getAttribute());
							
							if(op.equals("<=")) {
								if(v <= slos.get(j).getValue()) {
									
									dbmatrix[i][j] = v;
								}
							} else {
								if(op.equals(">=")) {
									if(v >= slos.get(j).getValue()) {
										
										dbmatrix[i][j] = v;
									}
								}
							}
								
						} 
					} catch (NumberFormatException e) {
						
						e.printStackTrace();
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
					*/
					
					System.out.println("==>" + Double.valueOf(references[i].getProperty(slos.get(j)
							.getAttribute()+"."+slos.get(j).getOperation()).toString()).doubleValue());
					
					System.out.println(slos.get(j)
							.getAttribute()+"."+slos.get(j).getOperation());
					
					matrix[i][j] = Double.valueOf(references[i].getProperty(slos.get(j)
							.getAttribute()+"."+slos.get(j).getOperation()).toString()).doubleValue();
					
				} else {
					
					/*
					try {
						if(joint.existService(Double.parseDouble((references[i].getProperty(Constants.SERVICE_ID)).toString()))) {
							
							String op = slos.get(j).getExpression().getOperator();
							
							double v = joint.getValueAttributeService(Double.parseDouble((references[i].getProperty(Constants.SERVICE_ID)).toString()), 
									slos.get(j).getAttribute());
							
							if(op.equals("<=")) {
								if(v <= slos.get(j).getValue()) {
									
									dbmatrix[i][j] = v;
								}
							} else {
								if(op.equals(">=")) {
									if(v >= slos.get(j).getValue()) {
										
										dbmatrix[i][j] = v;
									}
								}
							}
						}
					} catch (NumberFormatException e) {
						
						e.printStackTrace();
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
					*/
					
					System.out.println("==>" + Double.valueOf(references[i].getProperty(slos.get(j).
							getAttribute()).toString()).doubleValue());
					
					System.out.println(slos.get(j).getAttribute());
					
					matrix[i][j] = Double.valueOf(references[i].getProperty(slos.get(j).
							getAttribute()).toString()).doubleValue(); 
					
				}
			}
		}
		
		//if(dbmatrix.length != 0) {
			//return dbmatrix;
			
		//} else {
			return matrix;
		//}
	}
	
	private double[] min(List<Slo> slos, ServiceReference[] references,
			double[][] matrix) {
		double[] min = new double[slos.size()];
		
		for(int i=0; i<references.length; i++) {
			for(int j=0; j<slos.size(); j++) {
				
				if(i==0) {
					min[j] = Double.MAX_VALUE;
				}
				
				if(matrix[i][j] < min[j]) {
					min[j] = matrix[i][j];
				}
			}
		}
		return min;
	}
	
	private double[] max(List<Slo> slos, ServiceReference[] references,
			double[][] matrix) {
		double[] max = new double[slos.size()];
		
		for(int i=0; i<references.length; i++) {
			for(int j=0; j<slos.size(); j++) {
				
				if(matrix[i][j] > max[j]) {
					max[j] = matrix[i][j];
				}
			}
		}
		return max;
	}
	
	public double[] normalizedSlos(List<Slo> slos) {
		double[] norm =  new double[slos.size()];
		
		for(int i=0; i<slos.size(); i++) {
			String direction = slos.get(i).getExpression().getOperator();
			
			if(direction.equals("<=")) {
				if((max[i]-min[i]) == 0) {
					norm[i] = 1;
				} else {
					norm[i] = (max[i]-slos.get(i).getValue())/(max[i]-min[i]);
				}
				
			} else {
				if(direction.equals(">=")) {
					if((max[i]-min[i]) == 0) {
						norm[i] = 1;
					} else {
						norm[i] = (slos.get(i).getValue()-min[i])/(max[i]-min[i]);
					}
					
				}
			}
		}
		return norm;
	}
}
