# Test Assignment

There is a class `ChargePlanner` that should calculate the optimal charging schedule for charging the electrical vehicle. The electrical vehicle charging schedule is considered to be optimal when:
- vehicle is charged when the energy market price is the lowest (that results into the lowest charging expenses)
- vehicle is charged to the required level as early as possible but still taking the previous point into the account

Energy prices are published before-hand and we will assume they are defined per-hour only. 

There is the basic test `chargePlannerReturnsDefinedPlanForDefinedPrices` implemented and failing at the moment. Test is testing strictly defined pre-calculated test case. 

The defined test calculation is provided in the table below.

| | 1:00 | 2:00 | 3:00 | 4:00 | 5:00 | 6:00 | 7:00| 8:00 | 9:00
--- | --- | --- | --- | --- | --- | --- | --- | --- | --- 
Buying Price |13|10|8|10|8|10|11|15|Car Ready
Selling Price|10|9|7|9|7|8|9|13 
Battery Level|20|20|20|70|70|80|80|80|80
Capacity| | |50| |10|


The assignment is to implement the method that will work for given pre-calculated test case, and that will also work for any randomly generated energy price sequences.

Please make `chargePlannerReturnsDefinedPlanForDefinedPrices` test pass to make sure that the method implementation is really working for pre-calculated test case.

Please implement the unit tests that will make sure that this method is really working for any randomly generated energy price sequences, too.

Additionally, you could implement the method that will calculate the amount of saved money that the `ChargePlanner` generates comparing with classical way to charge when the changing would start straight away when connected to the charger, and will charge until charged to maximum.

#below section written by the developer A K M Shariful Islam

1. The charge planner method has been implemented successfully for the given test case.
2. It also should supposed to work for any random generated energy sequences. 
3. A test method for another energy sequences is also given
4. My understanding from the given test cases is that the askedPrice is used to calculate cost and I did all my work with that undestanding
5. If the energy price sequences is given for multiple days then vehicle would still wait if needed for the lowest optimum cost 
6. Description is given before each method
7. The maximum charge power will be used for the lowest cost even if it comes later on schedule.
8. The assumption is that "there is some mechanism by which one can control the amount of charge given in a hour"
9. additional method like cost saving by charge planner is also given
10. also test method is also given for testing cost saving.