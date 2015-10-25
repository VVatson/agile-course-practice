package ru.unn.agile.statistics.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class WhenCalculateStatisticValues {
    @Before
    public void Preparing()
    {
        statVal = new StatisticValues();
        dataInstances = null;
    }

    @Test
    public void enumerationOfSimpleIntArrayIsCorrect(){
        int[] data = {5, 5, 5, 5};
        formDataInstances(data);

        checkEnumerationWith(5.0);
    }

    @Test
    public void enumerationOfIntegerArrayWithDifferentElementsIsCorrect()
    {
        int[] data = new int[100];
        for(int i = 0; i < data.length; i++){
            data[i] = i;
        }
        formDataInstances(data);

        checkEnumerationWith(49.5);
    }

    @Test
    public void enumerationOfDoubleArrayWithDataOfOneSinPeriodIsCorrect()
    {
        double[] data = new double[1000];
        for(int i = 0; i < data.length; i++){
            data[i] = Math.sin(i*2*Math.PI/1000.0);
        }
        formDataInstances(data);

        checkEnumerationWith(0.0);
    }

    @Test
    public void enumerationOfFloatArrayWithOneElementIsCorrect(){
        float[] data = {3.14f};
        formDataInstances(data);

        checkEnumerationWith(3.14f);
    }

    @Test
    public void enumerationIsZeroWhenStatisticDataIsEmpty() {
        statVal.getData(null);
        checkEnumerationWith(0.0);
    }

    @Test
    public void statisticDataIsEmptyWhenConvertingIntArrayIsEmpty(){
        Collection<IStatisticDataInstance> dataInstances = StatisticDataConverter.ConvertFromIntArray(null);
        assertEquals(dataInstances, null);
    }

    @Test
    public void probabilityOfSingleEventEqualsToOne(){
        int[] data = {7};
        IStatisticDataInstance event = new NumericStatisticDataInstance(data[0]);
        formDataInstances(data);

        checkProbabilityWith(1, event);
    }

    @Test
    public void probabilityOfImpossibleEventEqualsToZero(){
        float[] data = {1.f,2.f,3.f,4.1f,5.f,6.f,7.f,8.f,9.f,10.f};
        IStatisticDataInstance event = new NumericStatisticDataInstance(4);
        formDataInstances(data);

        checkProbabilityWith(0.0, event);
    }

    @Test
    public void probabilityOfSomePossibleEventIsCorrect(){
        double[] data = {-1., 3.14, 5., 7.13, 8.25, 3.14};
        IStatisticDataInstance event = new NumericStatisticDataInstance(3.14f);
        formDataInstances(data);

        checkProbabilityWith(1./3, event);
    }

    @Test
    public void probabilityWithoutDataIsZero(){
        double[] data = null;
        IStatisticDataInstance event = new NumericStatisticDataInstance(0);
        formDataInstances(data);

        checkProbabilityWith(0.0, event);
    }

    @Test
    public void varianceOfConstantDataIsZero(){
        int[] data = {1, 1, 1, 1, 1};
        formDataInstances(data);

        checkVarianceWith(0);
    }

    @Test
    public void varianceWithoutDataIsZero(){
        int[] data = null;
        formDataInstances(data);

        checkVarianceWith(0);
    }

    @Test
    public void varianceOfDataRepresentsOneCosPeriodIsEqualToOneHalf(){
        double[] data = new double[1000];
        for(int i = 0; i < data.length; i++){
            data[i] = Math.cos(i * 2 * Math.PI / 1000.0);
        }
        formDataInstances(data);

        checkVarianceWith(1./2);
    }

    @Test
    public void rawMomentOfThirdOrderWithoutDataIsZero(){
        int[] data = null;
        formDataInstances(data);

        double rawMoment = statVal.rawMoment(3);
        assertEquals(rawMoment, 0.0, deltaForDoubleAssertEquals);
    }

    @Test
    public void rawMomentOfFirstOrderIsEqualToEnumeration(){
        double[] data = new double[1000];
        for(int i = 0; i < data.length; i++){
            data[i] = Math.log10((i + 1) * Math.PI / 1000.0);
        }
        formDataInstances(data);

        double enumeration = statVal.enumeration();
        double rawMoment = statVal.rawMoment(1);
        assertEquals(rawMoment, enumeration, deltaForDoubleAssertEquals);
    }

    @Test
    public void rawMomentOfSecondOrderSmallerThanFourthOrderWhenDataHasBigVariance(){
        int[] data = {-1, -1, 5, 8, 10, 4, 4, 8};
        formDataInstances(data);

        double rawMoment2 = statVal.rawMoment(2);
        double rawMoment4 = statVal.rawMoment(4);
        assertTrue(rawMoment2 < rawMoment4);
    }

    @Test
    public void rawMomentOfSixOrderBiggerThanEighthOrderWhenDataHasSmallVariance(){
        double[] data = {-0.1, -0.1, 0.5, 0.8, 1.0, 0.4, 0.4, 0.8};
        formDataInstances(data);

        double rawMoment6 = statVal.rawMoment(6);
        double rawMoment8 = statVal.rawMoment(8);
        assertTrue(rawMoment6 > rawMoment8);
    }

    private void checkEnumerationWith(final double destination)
    {
        double enumOfData = statVal.enumeration();
        assertEquals(destination, enumOfData, deltaForDoubleAssertEquals);
    }

    private void checkVarianceWith(final double destination)
    {
        double varOfData = statVal.variance();
        assertEquals(varOfData, destination, deltaForDoubleAssertEquals);
    }

    private void checkProbabilityWith(final double destination, final IStatisticDataInstance event)
    {
        double p = statVal.probabilityOf(event);
        assertEquals(destination, p, deltaForDoubleAssertEquals);
    }

    private void formDataInstances(double[] data){
        Collection<IStatisticDataInstance> dataInstances = StatisticDataConverter.ConvertFromDoubleArray(data);
        statVal.getData(dataInstances);
    }

    private void formDataInstances(float[] data){
        Collection<IStatisticDataInstance> dataInstances = StatisticDataConverter.ConvertFromFloatArray(data);
        statVal.getData(dataInstances);
    }

    private void formDataInstances(int[] data){
        Collection<IStatisticDataInstance> dataInstances = StatisticDataConverter.ConvertFromIntArray(data);
        statVal.getData(dataInstances);
    }

    private StatisticValues statVal;
    private Collection<IStatisticDataInstance> dataInstances;
    private double deltaForDoubleAssertEquals = 1e-3;
}
