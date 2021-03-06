pragma solidity >=0.5.11;

//Write a constructor for the Money object shown above so that it accepts an integer as a parameter and sets "amount" to that integer value.

contract Money {
    int public amount;

    //TODO: write your constructor here
    constructor(int amt) public {
        amount = amt;
    }
}

contract Wallet {
    Money m; // m is owned

    constructor() public {
        m = new Money(0); //TODO: you will have to change this once you make your new constructor
    }

    function spendMoney() public {
        //...
    }

    // mon is owned at the beginning but unowned at the end
    // Returns an owned reference.
    function receiveMoney(Money mon) public returns (Money) {
        Money temp = m;
        m = mon;
        return temp;
    }

}