package ee.energia.testassignment;

import ee.energia.testassignment.planning.ChargePlan;
import ee.energia.testassignment.price.EnergyPrice;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ChargePlanner {

    // the capability of the charger to charge certain amount of energy into the battery in 1 hour
    public final static int CHARGER_POWER = 50;

    // maximum battery level possible
    public final static int MAX_LEVEL = 100;

    // battery level required by the end of the charging
    public final static int REQUIRED_LEVEL = 100;

    /**
     * Method calculates the optimal hourly charge plan.
     * Method finds the cheapest hour to charge the battery (if multiple then the earliest)
     * and uses it to charge the battery up to the {@link ChargePlanner#REQUIRED_LEVEL}.
     * If {@link ChargePlanner#CHARGER_POWER} limitation does not allow to do this in one hour,
     * then method finds the next cheapest opportunities and uses them until {@link ChargePlanner#REQUIRED_LEVEL} is met.
     * <p>
     * Method returns the array of {@link ChargePlan} objects that represent the hourly time slot
     * and the capacity that we need to charge during that hour to charge the battery.
     *
     * @param batteryLevel initial battery level when the charger is connected
     * @param energyPrices the list of the energy prices from the moment when charger is connected until the moment when battery needs to be charged
     *                     there is an assumption that battery is connected the first second of the first given hour and disconnected the last second of the last given hour
     * @return
     */
    public static ArrayList<ChargePlan> calculateChargePlan(int batteryLevel, ArrayList<EnergyPrice> energyPrices) {
        // todo: implement the function that will be calculating the optimal hourly charge plan
        ArrayList<ChargePlan> chargePlans = new ArrayList<>();
        //mapping with ask price all the charge plans;
        Map<Integer, List<EnergyPrice>> sortedMappedEnergyPricesOnLowestPrice = mapEnergyPriceConsideringLowestPrice(energyPrices);
        Iterator it = sortedMappedEnergyPricesOnLowestPrice.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry eachPairFromPriceMappedMap = (Map.Entry) it.next();
            List<EnergyPrice> sameCostEnergyPriceListSeparatedByHour = sortedEnergyPriceList(eachPairFromPriceMappedMap);
            for (EnergyPrice energyPrice : sameCostEnergyPriceListSeparatedByHour
            ) {
                int capacity = Math.min(REQUIRED_LEVEL - batteryLevel, CHARGER_POWER);
                chargePlans.add(new ChargePlan(capacity, energyPrice.getHour(), energyPrice.getMonth(), energyPrice.getYear()));
                batteryLevel = capacity + batteryLevel;
            }
            // avoids a ConcurrentModificationException
            it.remove();
        }
        chargePlans.sort(Comparator.comparing(ChargePlan::getHour));
        return chargePlans;
    }

    /**
     * Method sorts the energy price list based on year, month, day and hour of a certain cost.
     * Method returns the sorted List of {@link EnergyPrice} objects
     *
     * @param eachPairFromPriceMappedMap the HashMap entry contain the energy price list of a certain cost
     */
    private static List<EnergyPrice> sortedEnergyPriceList(Map.Entry eachPairFromPriceMappedMap) {
        List<EnergyPrice> sameCostEnergyPriceListSeparatedByHour = (List<EnergyPrice>) eachPairFromPriceMappedMap.getValue();
        sameCostEnergyPriceListSeparatedByHour
                .sort(
                        Comparator.comparing(EnergyPrice::getYear)
                                .thenComparing(EnergyPrice::getMonth)
                                .thenComparing(EnergyPrice::getDay)
                                .thenComparing(EnergyPrice::getHour)
                );
        return sameCostEnergyPriceListSeparatedByHour;
    }


    /**
     * Method maps  the energy price list based on {@link EnergyPrice::getAskPrice} to a HashMap
     * Method returns the sorted TreeMap of mapped energy price on askedPrice
     *
     * @param energyPrices the List of given Energy Price list
     */
    private static Map<Integer, List<EnergyPrice>> mapEnergyPriceConsideringLowestPrice(List<EnergyPrice> energyPrices) {
        Map<Integer, List<EnergyPrice>> mappedEnergyPrices = energyPrices
                .stream()
                .collect(
                        Collectors.groupingBy
                                (EnergyPrice::getAskPrice)
                );

        Map<Integer, List<EnergyPrice>> sortedMappedEnergyPricesOnLowestPrice = new TreeMap<Integer, List<EnergyPrice>>(mappedEnergyPrices);
        return sortedMappedEnergyPricesOnLowestPrice;

    }

    /**
     * Method finds  the charge plan from the  list that have capacity more than 0. in other words it finds the used charge plan
     * Method @return the list of used {@link ChargePlan}
     *
     */
    private static List<ChargePlan> getUsedChargePlan(List<ChargePlan> chargePlans) {
        return chargePlans
                .stream()
                .filter(t -> t.getCapacity() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Method calculates  the cost of {@link ChargePlan} by given charge plan list
     * Method @return the charge plan cost in Integer format.
     * @param chargePlans - the list of {@link ChargePlan}
     * @param energyPrices - the list of {@link EnergyPrice}
     */
    private static Integer calculateChargePlanCost(List<ChargePlan> chargePlans,List<EnergyPrice> energyPrices){
        int cost=0;
        for (ChargePlan chargePlan : chargePlans
        ) {

            EnergyPrice energyPrice = energyPrices
                    .stream()
                    .filter(t ->
                            t.getYear() == chargePlan.getYear()
                                    && t.getMonth() == chargePlan.getMonth()
                                    && t.getHour() == chargePlan.getHour()
                    )
                    .findFirst()
                    .orElse(null);
            cost += energyPrice.getAskPrice();

        }
        return cost;

    }

    /**
     * Method calculates  the cost of {@link ChargePlan} generated by charge planner
     * Method @return the charge plan cost in Integer format.
     * @param chargePlans - the list of {@link ChargePlan}
     * @param energyPrices - the list of {@link EnergyPrice}
     */
    public static Integer getChargePlannerCost(List<ChargePlan> chargePlans, List<EnergyPrice> energyPrices) {
        List<ChargePlan> usedChargePlan =getUsedChargePlan(chargePlans);
        int cost = calculateChargePlanCost(usedChargePlan,energyPrices);
        return cost;

    }

    /**
     * Method calculates  the cost of {@link ChargePlan}  generated by classical way
     * Method @return the charge plan cost in Integer format.
     * @param chargePlans - the list of {@link ChargePlan}
     * @param energyPrices - the list of {@link EnergyPrice}
     */
    public static Integer getClassicalPlanCost(List<ChargePlan> chargePlans, List<EnergyPrice> energyPrices) {
        List<ChargePlan> usedChargePlan=getUsedChargePlan(chargePlans);
        int cost = calculateChargePlanCost(usedChargePlan,energyPrices);
        return cost;

    }

    /**
     * Method generates charge plan list in classical way
     * Method @return the list of {@link ChargePlan}.
     * @param batteryLevel - the level of current battery power of vehicle
     * @param energyPrices - the list of  given {@link EnergyPrice}
     */
    public static List<ChargePlan> getClassicalChargePlan(int batteryLevel, List<EnergyPrice> energyPrices) {
        List<ChargePlan> chargePlans = new ArrayList<>();
        for (EnergyPrice energyPrice : energyPrices
        ) {

            int capacity = Math.min(REQUIRED_LEVEL - batteryLevel, CHARGER_POWER);
            chargePlans.add(new ChargePlan(capacity, energyPrice.getHour(), energyPrice.getMonth(), energyPrice.getYear()));
            batteryLevel = capacity + batteryLevel;
        }
        return chargePlans;
    }

}
