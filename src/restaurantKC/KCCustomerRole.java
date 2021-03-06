package restaurantKC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import restaurantKC.gui.CustomerGui;
import restaurantKC.interfaces.Cashier;
import restaurantKC.interfaces.Customer;
import restaurantKC.interfaces.Host;
import restaurantKC.interfaces.Waiter;
import city.PersonAgent;
import city.Role;

/**
 * Restaurant customer agent.
 */
public class KCCustomerRole extends Role implements Customer {
        private String name;
        private int hungerLevel = 4; // determines length of meal
        private String choice;
        Timer timer = new Timer();
        private CustomerGui customerGui;
        private boolean reorder = false;
        private double money;
        public boolean oweMoney = false;
        private double chanceOfStaying = 1;

        public List<String> availableOptions = new ArrayList<String>();



        // agent correspondents
        private Host host;
        private Waiter waiter;
        private Cashier cashier;

        private Menu myMenu;
        Check check;

        public enum AgentState
        {DoingNothing, WaitingInRestaurant, BeingSeated, OrderingFood, WaitingForFood, Eating, DoneEating, Leaving, WaitingForCheck, DoingAbsolutelyNothing};
        private AgentState state = AgentState.DoingNothing;//The start state

        public enum AgentEvent 
        {none, gotHungry, followWaiter, seated, order, foodReceived, foodUnavailable, doneEating, doneLeaving, checkArrived, punish, decidedToWait, leftEarly};
        AgentEvent event = AgentEvent.none;

        /**
         * Constructor for CustomerAgent class
         *
         * @param name name of the customer
         * @param gui  reference to the customergui so the customer can send it messages
         */
        public KCCustomerRole(PersonAgent person){
                super(person);
                this.name = super.getName();
                if ((name.equals("flake")) || (name.equals("Flake"))) {
                        money = 0;
                }
                else
                        money = 100;
        }

        /**
         * hack to establish connection to Host agent.
         */
        public void setHost(Host host) {
                this.host = host;
        }

        public void setWaiter(Waiter waiter) {
                this.waiter = waiter;
        }

        public String getCustomerName() {
                return name;
        }
        // Messages

        public void msgGotHungry() {
                print("I'm hungry");
                event = AgentEvent.gotHungry;
                stateChanged();
        }

        public void msgFollowMe(Menu menu) {

                myMenu = menu;
                // Customer browses menu to see what he/she can afford
                availableOptions.clear();
                for (int i = 0; i < myMenu.menuItems.length; i++) {
                        if (myMenu.priceMap.get(myMenu.menuItems[i]) <= money) {
                                availableOptions.add(myMenu.menuItems[i]);
                        }
                }
                reorder = false;

                event = AgentEvent.followWaiter;
                stateChanged();
        }

        public void msgWhatWouldYouLike() {
                event = AgentEvent.order;
                stateChanged();
        }

        public void msgHereIsYourFood() {
                event = AgentEvent.foodReceived;
                stateChanged();
        }

        public void msgFoodUnavailable() {
                event = AgentEvent.foodUnavailable;
                availableOptions.remove(choice);
                reorder = true;
                stateChanged();
        }

        public void msgHereIsCheck(Check c) {
                check = c;
                event = AgentEvent.checkArrived; 
                stateChanged();
        }
        
        public void msgHereIsYourChange(double change) {
                money += change;
        }

        public void msgAnimationFinishedGoToSeat() {
                //from animation
                event = AgentEvent.seated;
                stateChanged();
        }

        public void msgAnimationFinishedLeaveRestaurant() {
                //from animation
                event = AgentEvent.doneLeaving;
                stateChanged();
        }

        public void msgPunish() {
                event = AgentEvent.punish;
                oweMoney = true;
                stateChanged();
        }


        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
        public boolean pickAndExecuteAnAction() {
                //        CustomerAgent is a finite state machine

                if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
                        state = AgentState.WaitingInRestaurant;
                        GoToRestaurant();
                        return true;
                }

                if (state == AgentState.DoingAbsolutelyNothing && event == AgentEvent.gotHungry ){
                        state = AgentState.WaitingInRestaurant;
                        GoToRestaurant();
                        return true;
                }

                if (state == AgentState.WaitingInRestaurant && event == AgentEvent.gotHungry) {
                        if(Math.random() <= chanceOfStaying)
                        {
                                event = AgentEvent.decidedToWait;
                                return true;
                        }
                        else 
                        {
                                state = AgentState.DoingNothing;
                                event = AgentEvent.leftEarly;
                                LeaveTableWithoutEating();
                                return true;
                        }
                }

                if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
                        state = AgentState.BeingSeated;
                        SitDown();
                        return true;
                }

                if (state == AgentState.BeingSeated && event == AgentEvent.seated){
                        state = AgentState.OrderingFood;
                        CallWaiter();
                        return true;
                }

                if (state == AgentState.OrderingFood && event == AgentEvent.order){
                        state = AgentState.WaitingForFood;
                        OrderFood();
                        return true;
                }

                if (state == AgentState.WaitingForFood) {

                        if (event == AgentEvent.foodReceived){
                                state = AgentState.Eating;
                                EatFood();
                                return true;

                        }

                        if (event == AgentEvent.foodUnavailable) {
                                state = AgentState.OrderingFood;
                                event = AgentEvent.order;
                                print ("Reordering");
                                return true;
                        }

                }


                if (state == AgentState.Eating && event == AgentEvent.doneEating){
                        askForCheck();
                        state = AgentState.WaitingForCheck;
                        return true;
                }

                if ((state == AgentState.WaitingForCheck) && (event == AgentEvent.checkArrived)) {
                        payCheckAndLeave();
                        state = AgentState.DoingNothing;
                        return true;
                }

                if ((state == AgentState.DoingNothing) && (event == AgentEvent.punish)) {
                        stealMoney();
                        state = AgentState.DoingAbsolutelyNothing;
                        return true;
                }


                return false;
        }

        // Actions

        private void GoToRestaurant() {
                host.msgIWantFood(this);//send our instance, so he can respond to us
        }

        private void SitDown() {
                customerGui.DoGoToSeat();//hack; only one table
        }

        private void CallWaiter() {
                waiter.msgImReadyToOrder(this);
        }

        private void OrderFood() {

                if ((name.equals("flake")) || (name.equals("Flake"))) {
                        Random randomGenerator = new Random();
                        int randomInt = randomGenerator.nextInt(4);
                        choice = myMenu.menuItems[randomInt];
                        print ("I would like to order " + choice); 
                        waiter.msgHereIsMyChoice(choice, this);
                }

                else {
                        if (availableOptions.size() <= 0) {
                                state = AgentState.DoingNothing;
                                LeaveTable();
                        }
                        else if ((name.equals("salad")) || (name.equals("Salad")) || (name.equals("steak")) || (name.equals("Steak"))  || (name.equals("pizza")) || (name.equals("Pizza")) || (name.equals("chicken")) || (name.equals("Chicken")))
                        {
                                if (!reorder) {
                                        choice = name;
                                        if (choice.equals("steak"))
                                                choice = "Steak";
                                        if (choice.equals("pizza"))
                                                choice = "Pizza";
                                        if (choice.equals("salad"))
                                                choice = "Salad";
                                        if (choice.equals("chicken"))
                                                choice = "Chicken";

                                        if (availableOptions.contains(choice)) {
                                                print ("I would like to order " + choice); 
                                                waiter.msgHereIsMyChoice(choice, this);
                                        }
                                }

                                if (reorder) {
                                        Random randomGenerator = new Random();
                                        int randomInt = randomGenerator.nextInt(availableOptions.size());
                                        choice = availableOptions.get(randomInt);
                                        waiter.msgHereIsMyChoice(choice, this);
                                        print ("I would like to order " + choice);
                                }
                        }
                        else {
                                Random randomGenerator = new Random();
                                int randomInt = randomGenerator.nextInt(availableOptions.size());
                                choice = availableOptions.get(randomInt);
                                waiter.msgHereIsMyChoice(choice, this);
                                print ("I would like to order " + choice);
                        }
                }
        }


        private void EatFood() {
                print("Eating Food");
                timer.schedule(new TimerTask() {
                        public void run() {
                                print("Done eating");
                                event = AgentEvent.doneEating;
                                stateChanged();
                        }
                },
                getHungerLevel() * 1000);
        }

        private void askForCheck() {
                waiter.msgDoneEating(this); 
        }

        private void payCheckAndLeave() {

                timer.schedule(new TimerTask() {
                        public void run() {

                                if (check.price > money) {
                                        cashier.msgPayingCheck(check, money-check.price);
                                        print ("Couldn't afford check");
                                }
                                else {
                                        cashier.msgPayingCheck(check, check.price);
                                        print("Paid check of " + check.price + " for " + choice);
                                }
                                oweMoney = false;
                                LeaveTable();
                                print("Leaving the restaurant");

                                customerGui.DoExitRestaurant(); //for the animation

                        }},
                        500);
        }


        private void LeaveTable() {
                print("Leaving.");
                waiter.msgLeaving(this);
                customerGui.DoExitRestaurant();
                
                person.exitBuilding();
                person.msgFull();
                doneWithRole();
        }

        private void LeaveTableWithoutEating() {
                print ("Leaving");
                if (waiter != null)
                        waiter.msgLeaving(this);
                else
                        host.msgLeaving(this);
        }

        public String getName() {
                return name;
        }

        public int getHungerLevel() {
                return hungerLevel;
        }

        public void setHungerLevel(int hungerLevel) {
                this.hungerLevel = hungerLevel;
                //could be a state change. Maybe you don't
                //need to eat until hunger lever is > 5?
        }

        public String toString() {
                return "customer " + getName();
        }

        public void setCashier(Cashier c) {
                cashier = c;
        }

        public void setGui(CustomerGui g) {
                customerGui = g;
        }

        public CustomerGui getGui() {
                return customerGui;
        }

        // If customer doesn't have enough money, he/she steals it. 
        private void stealMoney() {
                print ("Stole 100 dollars");
                money += 100;
        }

        @Override
        public void startThread() {
                // TODO Auto-generated method stub
                
        }


        


}
