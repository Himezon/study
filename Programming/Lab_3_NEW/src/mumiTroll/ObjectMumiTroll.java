package mumiTroll;

import backgroundObject.BackgroundObject;
import backgroundObject.Water;

import java.util.Objects;

public class ObjectMumiTroll{

    private String name;
    public int health;
    private String skinThickness;

    public ObjectMumiTroll(String name){
        this.name = name;
    }


    public String bury(BackgroundObject backgroundObject){
        return name + " buried in the " + backgroundObject.getName();
    }

    public String getName(){
        return name;
    }

    public int getHealth(){
        return health;
    }

    public void checkStatus(){
        if (getHealth() <= 0){
            System.out.println( "Hordes of mosquitoes bit through the skin of a MumiTroll");
        } else {
            System.out.println( "Hordes of mosquitoes did not bite through the skin of the MumiTroll");
        }
    }

    public String dizzy(Water water){
        if (water.speedMeter()){
            return name + " dizzy";
        } else {
            return name + " don't feel dizzy";
        }
    }

    public void setHealth(int health){
        this.health = health;
    }

    public void setSkinThickness(String skinThickness){
        this.skinThickness = skinThickness;
        if (skinThickness.equals("low")){
            this.setHealth(1);
        } else {
            if (skinThickness.equals("middle")){
                this.setHealth(2);
            } else {
                if (skinThickness.equals("hard")){
                    this.setHealth(3);
                }
            }
        }
    }

    @Override
    public int hashCode(){
        return Objects.hash(name, skinThickness, health);
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        if (this == obj)
            return true;
        ObjectMumiTroll other = (ObjectMumiTroll) obj;
        return Objects.equals(name, other.name) && Objects.equals(skinThickness, other.skinThickness) && health == other.health;
    }
}
