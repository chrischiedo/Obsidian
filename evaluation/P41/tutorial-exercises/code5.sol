pragma solidity >=0.5.11;

//A Drink can be hot, cold, or lukewarm. If the Drink is cold, it has an IceCube object. If it is hot, it has a CupSleeve.
//The Drink always has an integer value to keep track of its temperature.
//A Drink always starts out lukewarm.

//Write the Drink contract below with the necessary states, fields, and constructors for the situation described above.
//The contracts for IceCube and CupSleeve are given below.

//Note: the Drink contract MUST be main


contract IceCube {
    //...
}

contract CupSleeve {
    //...
}

contract Drink {
    enum State {hot, cold, lukewarm}
    int temperature;

    State state;
    Icecube icecube;
    Cupsleeve cupsleeve;


}