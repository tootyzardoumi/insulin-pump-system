/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import Model.sensor;
import java.util.logging.Level;
import java.util.logging.Logger;
import Events.bloodreading;
import Events.pumps;
import Esper.configs;
/**
 *
 * @author Lenovo
 */
class InsulinDose extends Thread {
    private float lastdose;
    private float currdose;
    private float cumulativeDoseinDay=5; 
    protected float maxreading=400;
    protected float minreading=130;
    protected float safeZone=180; 
    private int numberofdose;
   
    private insulinresrvoir resrvoir;
    
    private InsulinPumpSystem insulinpumpsystem;
    
    public InsulinDose(){
    //empty constructor
    }

   public InsulinDose(InsulinPumpSystem insulinpumpsystem) {
       
       this.insulinpumpsystem=insulinpumpsystem;
    }
    
     public float getLastdose() {
        return lastdose;
    }

    public void setLastdose(float lastdose) {
        this.lastdose = lastdose;
    }

    public float getCurrdose() {
        return currdose;
    }

    public void setCurrdose(float currdose) {
        this.currdose = currdose;
    }


    
    // calculate the dose based on the sugar read 
    // hint the dose calcualtion is based on hypothieses 
    public float mesuredose(sensor sen){
    if (sen.currentreading <=minreading){
        
        currdose=0;
        
    }
    else if(sen.currentreading>=maxreading)
    {  currdose=50;
    }
    else if(sen.currentreading>= safeZone && sen.currentreading<= maxreading)
    {
        currdose=40;
    }
        return currdose;
}
    //check how much remaining dose in the pump 
    public boolean doseAvailability (float availableDose){
    pump p = new pump(); //pump the current dose
    availableDose=resrvoir.getAmountofinsulin(); //get the available amount of insulin in resrvoir
    
    if (currdose<availableDose){
        p.pump(this,true);
       
        return true;
    }
    else
    {
    
    System.out.println("the amount of insulin in pump is less that the reqired dose");
    System.out.println("IMPORTANT!!!  please refill the reservoir");
    return false;
    }
    
}
 // caclualte amount of insulin in the reservoir
 public float checkAmountOfInsulinInResrvoir(){
    float insulinAmount= resrvoir.getAmountofinsulin();
    System.out.println("amount of insulin is"+ insulinAmount);
    return insulinAmount;
 }

   


@Override
public void run(){
    while(true){

        try{
        
            this.sleep(1000);
        
        } catch (InterruptedException ex){
        Logger.getLogger(InsulinDose.class.getName()).log(Level.SEVERE, null, ex);
        
        }
        configs.sendEvent(new pumps(currdose));
}


}




}