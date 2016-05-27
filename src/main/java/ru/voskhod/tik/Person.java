package ru.voskhod.tik;

public class Person {
    private String firstName;
    private String lastName;
    private String middleName;
    private Integer votes;

    public Person(String lastName, String firstName, String middleName, Integer votes ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.votes = votes;
    }
    
    public void setName(String lastName, String firstName, String middleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public String getName() {
        String result = this.lastName;
        if( !this.firstName.trim().equals("") ) {
            result += " " + this.firstName.substring(0, 1) + ".";
        }
        if( !this.middleName.trim().equals("") ) {
            result += this.middleName.substring(0, 1) + ".";
        }
        if( result.length() > 20 ) {
            result = result.substring(0, 20);
        } else {
            //result = String.format("%1$-20s", result);
            int i = 20 - result.length();
            while(i>0) {
                i--;
                if(i%2 == 0) {
                    result += " ";
                } else {
                    result = " " + result;
                }
            }
        }
        return result;
    }

    public void setVotes(Integer votesParam) {
        votes = votesParam;
    }

    public Integer getVotes() {
        return this.votes;
    }
}
