package de.fhdortmund.jk.autofinanzierung.beans.interfaces;

public interface CarFinancing {
	public double computeNetLoanAmount(double price, double firstInstallment);
	
	public double computeGrossLoanAmount(double price, double firstInstallment, int paymentTerm);

	public double computeMonthlyPayment(double price, double firstInstallment, int paymentTerm);

	public double getInterestRate();
}
