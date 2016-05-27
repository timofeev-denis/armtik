package ru.voskhod.tik;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class PersonsCollection {
    private ArrayList<Person> persons;
    
    public PersonsCollection() {
        this.persons = new ArrayList<>();
    }
    
    public void addPerson(Person p) {
        this.persons.add(p);
    }
    
    public String getLabels(int first, int last) {
        String result = "";
        
        if (this.persons.size() == 0 || last >= this.persons.size() || first < 0 ) {
            return "\"                    \", \"\", \"\", \"\", \"\"";
        }
        //for(Person p : this.persons) {
        for(int i = first; i <= last; i++) {
            result += "\"" + persons.get(i).getName() + "\", ";
        }
        result = result.substring(0, result.length() - 2); // Отрезаем запятю и пробел
        return result;
    }
    
    public Integer getTotalVotes() {
        Integer result = 0;
        for(Person p : this.persons) {
            result += p.getVotes();
        }
        return result;
    }
    
    public String getVotes(int first, int last) {
        String result = "";
        if (this.persons.size() == 0 || last >= this.persons.size() || first < 0 ) {
//            return "";
            return "1, 2, 3, 4, 5";
        }
        if( getTotalVotes() == 0 ) {
//            return result;
            for(int i = first; i <= last; i++) {
                result += "0, ";
            }
            result = result.substring(0, result.length() - 2); // Отрезаем запятю и пробел
            return result;
        }
        DecimalFormat df = new DecimalFormat("##.#");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        double percents;
        for(int i = first; i <= last; i++) {
            percents = (double) persons.get(i).getVotes() / getTotalVotes() * 100;
            result += df.format(percents).replace(",", ".") + ", ";
        }
        result = result.substring(0, result.length() - 2); // Отрезаем запятю и пробел
        return result;
    }
    
    public String getBarMax() {
        String result = "100";
        if (this.persons.size() == 0 || getTotalVotes() == 0) {
            return "100";
        }
        DecimalFormat df = new DecimalFormat("##.#");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        double percents = 0, tmp;
        for(Person p : persons) {
            tmp = (double) p.getVotes() / getTotalVotes() * 100;
            if (tmp > percents) {
                percents = tmp;
            }
        }
        if( percents > 0 ) {
            result = df.format(percents).replace(",", ".");
        }
        return result;
        
    }
}
