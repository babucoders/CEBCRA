/*
 * EventHandlerA3.java
 *
 * Created on February 8, 2007, 2:48 PM
 *
This file is part of Atarraya.

Atarraya is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Atarraya is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Atarraya.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 Pedro M. Wightman
 */

package atarraya.event;

import atarraya.constants;
import atarraya.element.candidate;
import atarraya.element.node;
import atarraya.element.register;
import atarraya.atarraya_frame;

/**
 *
 * @author Pedro
 */


public class EventHandlerSGTTRot implements EventHandlerSW,constants{
    
    atarraya_frame father;
    
    
    int type = TM_PROTOCOL;
    int TMType;
    int temp_address=0;
            
    double NEXT_RESET_TIMEOUT;
    
    /** Creates a new instance of EventHandlerA3 */
    public EventHandlerSGTTRot(atarraya_frame _father) {
        
        father = _father;    
        TMType = TM_TIME;
    }
    
    public int getTMType(){
        return TMType;
    }
    
    public void setLabels(int _initial,int _active, int _inactive, int _sleeping){ }   
    
    public int getTreeID(){
        return father.getTreeID();
    }
    
    public int getSortingMode(){
        return (int)father.getVariable(SORTINGMODE);
    }
    
    public int getSimMode(){
        return (int)father.getVariable(SIMMODE);
    }    
    
    public int getbatchMode(){
        return (int)father.getVariable(BATCH_SIMULATION);
    }
    
 public node getNode(int id){
        return father.getNode(id);
    }
    
    public void pushEvent(event_sim e){
        father.pushEvent(e);
    }
    
    public void AddTreeLine(int s, int d, int t){
        if(getbatchMode()==1){}
            //father.AddTreeLine(s,d,t);
    }
    
    public void RemoveTreeLine(int s, int d, int t){
        if(getbatchMode()==1){}
            //father.RemoveTreeLine(s,d,t);
    }
    
    public void frame_repaint(){
        if(getbatchMode()==1)
            father.frame_repaint();
        
    }
 
    public void InvalidateAllEventsFromIDFromTimeTOfCodeC(int id, double t, int c, int tree){
        father.InvalidateAllEventsFromIDFromTimeTOfCodeC(id,t,c,tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeT(int id, double t, int tree){
        father.InvalidateAllEventsFromIDFromTimeT(id,t,tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeTOfTypeTy(int id, double t, int Ty, int tree){
        father.InvalidateAllEventsFromIDFromTimeTOfTypeTy(id,t,Ty,tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(int id, double t, int Ty, int c, int tree){
        father.InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(id,t,Ty,c,tree);
    }
    
 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code){
        father.broadcast(final_time, current_time, _sender, _destination, _code,"",-1,type);
    }

 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, int _tree){
        father.broadcast(final_time, current_time, _sender, _destination, _code,"",_tree,type);
    }
 
 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, String msg){
        father.broadcast(final_time, current_time, _sender, _destination, _code,msg,type);
    }
    
 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, String msg, int _tree){
        father.broadcast(final_time, current_time,-1,-1, _sender, _destination, _code,msg,_tree,type);
    }
 
 public void broadcast(double final_time, double current_time, int _source, int _final_destination,int _sender, int _destination, int _code, String msg, int _tree){
        father.broadcast(final_time, current_time,_source,_final_destination, _sender, _destination, _code,msg,_tree,type);
    }
 
 public void broadcast(double final_time, double current_time, int _source, int _final_destination,int _sender, int _destination, int _code, String msg, int _tree, int _type){
        father.broadcast(final_time, current_time,_source,_final_destination, _sender, _destination, _code,msg,_tree,_type);
    }
 
 public double getRandom(double max_val){
        return java.lang.Math.random()*max_val;
    }
 
 public void showMessage(String m){
        father.showMessage(m);
    }
  
    
    public void init_nodes(int _treeID){
       
    }
    
    public void init_node(int _treeID, int id_node){
        
    }
    
    public void initial_event(int _id, int _treeID){
        double _clock = father.getVariable(CLOCK);
        int temp = getNode(_id).where_am_I_sink();
        
        NEXT_RESET_TIMEOUT = father.getVariable(NEXT_RESET_PERIOD);
        
        InvalidateAllEventsFromIDFromTimeTOfTypeTy(_id, _clock+DELTA_TIME, type,_treeID);
        
        if(_treeID == temp){
            //Program the neighborhood verification
            pushEvent(new event_sim(_clock+NEXT_HELLO_TIMEOUT, _clock, _id, _id, SEND_HELLO_MESSAGE_TM, "",temp,type));
        }
        
        //In this version, there will be an order of dominance of the coordination of the rotation: sink 0 first, sink 1 then...
        // The tree that dies will stop programming SEND_RESET_MESSAGE events, leaving it to the next sink alive, until there are no more.
        // It is assumed that all sink nodes are synchronized or that they can at least communicate through an external network.
        
        //Program the next topology creation
        pushEvent(new event_sim(_clock+NEXT_RESET_TIMEOUT+_treeID, _clock, _id, _id, SEND_RESET_MESSAGE, "",0,type));
        
    }
    
    public boolean CheckIfDesiredFinalState(int s){
        //if(s==S_PARENT || s==S_SLEEPING)
        //    return true;
        
        return false;
    }
    
    public boolean CheckIfDesiredSleepingState(int s){
        
        return false;
    }
    
    public boolean verifIfNodeInCDS(int _id, int _treeID){
    
        //if(getNode(_id).isParent(_treeID) || getNode(_id).isSink(_treeID) || _treeID==-1)
        //    return true;
        
        return false;
    }
    
    public int GetMessageSize(int code){
        
        if(code==SON_RECOGNITION || code==DATA)
            return SIZE_LONG_PACKETS;
            
        return SIZE_SHORT_PACKETS;
    }
    
    public void HandleEvent(event_sim e){
    
        int code = e.getCode();
        int sender = e.getSender();
        int source = e.getSource();
        int final_destination = e.getFinalDestination();
        int receiver = e.getReceiver();
        int destination = e.getDestination();
        double temp_clock = e.getTime();
        String temp_data = e.getData();
        int temp_tree = e.getTree();
        int temp_data_int=0;
        int temp_data_int2=0;
        int temp_data_int3=0;
        String[] temp_data_array = new String[50];
        String[] temp_data_array2;
        
        int numneighb,i,j,temp_ID;
        int index,tam;
        int min_child;
        register temp_reg;
        int tempdist;
        int temp_level=0;
        int temptam=0;
        int tempmax,nmax;
        int temp=0;
        int temp2=0;
        int temp3=0;
        double temp_time;
        double metric=0;
        double y;
        boolean sw=true;
        boolean sw2;
        boolean sw3 = true;
        candidate temp_cand;

        if(receiver != -1)
            sw3 = getNode(receiver).isAlive();
        else
            sw3=false;

        if(sw3){
        
        switch(code){
             
            case INIT_NODE:
                init_node(temp_tree, receiver);
                break;
            
            case INIT_EVENT:
                if(getNode(sender).isSink())
                    initial_event(sender,temp_tree);
                break;
                
            case RESET_TM_PROTOCOL:
                //stop all future event from the TM protocol
                InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock+DELTA_TIME, type,temp_tree);
                break;
                
                
            case RESET_MESSAGE:
                
                try{
                    temp = Integer.parseInt(temp_data);
                }catch(Exception ex){ex.printStackTrace();}
                
                if(temp != getNode(receiver).getActiveTree() && !getNode(receiver).isSink()){
                    //assign the new active tree
                    getNode(receiver).setActiveTree(temp);

                    //If the node is active in this tree, it will turn on its radio
                    /*if(getNode(receiver).isActive(temp))
                        //getNode(receiver).setRadioOn(true);
                        getNode(receiver).setEnergyState(STATE_READY);
                    else
                        //getNode(receiver).setRadioOn(false);
                        getNode(receiver).setEnergyState(STATE_JUST_RADIO);
                    */
                    
                    InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock, SENSOR_PROTOCOL,temp_tree);

                    //Program the next query of the sensor
                    pushEvent(new event_sim(temp_clock+PROCESSING_DELAY, temp_clock, receiver, receiver, INIT_EVENT, temp_data,temp,SENSOR_PROTOCOL));

                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, final_destination, receiver, -1, RESET_MESSAGE, temp_data,temp_tree);
                    
                }
                
                break;
                
        
                case RESET_MESSAGE_SINK:
                
                try{
                    temp = Integer.parseInt(temp_data);
                }catch(Exception ex){ex.printStackTrace();}
                
                if(temp != getNode(receiver).getActiveTree()){
                    //assign the new active tree
                    getNode(receiver).setActiveTree(temp);

                    /*if(getNode(receiver).isActive(temp))
                        //getNode(receiver).setRadioOn(true);
                        getNode(receiver).setEnergyState(STATE_READY);
                    else
                        //getNode(receiver).setRadioOn(false);
                        getNode(receiver).setEnergyState(STATE_JUST_RADIO);
                    */
                    //Invalidate all sensor events from the previous tree and prepare for the next query of the sensor
                    InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock, SENSOR_PROTOCOL, temp_tree);

                    //InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock+DELTA_TIME, type,temp_tree);
                    //if(getNode(receiver).isSink(temp))
                      //  pushEvent(new event_sim(temp_clock+NEXT_HELLO_TIMEOUT, temp_clock, receiver, receiver, SEND_HELLO_MESSAGE_TM, "",temp,type));

                    
                    //Program the next query of the sensor
                    pushEvent(new event_sim(temp_clock+PROCESSING_DELAY, temp_clock, receiver, receiver, INIT_EVENT, temp_data,temp,SENSOR_PROTOCOL));

                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, final_destination, receiver, -1, RESET_MESSAGE, temp_data,temp_tree);
                    
                }
                
                break;
                
            case SEND_RESET_MESSAGE:
                
                //If the node have not received the Reset Message, it will be in charge of sending the reset message.
                if(temp_tree == getNode(receiver).getActiveTree()){
                
                    //Register the current state of the network
                    father.addStatToLifetimeLogEvent();
                    
                    
                    getNode(receiver).incrementActiveTree();
                    
                    temp = getNode(receiver).getActiveTree();
                    
                    InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock+DELTA_TIME, type,temp_tree);
                    
                    pushEvent(new event_sim(temp_clock+NEXT_RESET_TIMEOUT, temp_clock, receiver,receiver, SEND_RESET_MESSAGE, "",temp,type));
                    
                    //pushEvent(new event_sim(temp_clock+NEXT_HELLO_TIMEOUT, temp_clock, receiver, receiver, SEND_HELLO_MESSAGE_TM, "",temp,type));
                    if(getNode(receiver).isSink(temp)){
                        pushEvent(new event_sim(temp_clock+NEXT_HELLO_TIMEOUT, temp_clock, receiver, receiver, SEND_HELLO_MESSAGE_TM, "",temp,type));
                        father.addTMInvocation();
                    }
                    
                    pushEvent(new event_sim(temp_clock+PROCESSING_DELAY, temp_clock, sender, sender, INIT_EVENT, temp_data,temp,SENSOR_PROTOCOL));
                    
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, final_destination, receiver, -1, RESET_MESSAGE, ""+temp,temp);

                    pushEvent(new event_sim(temp_clock+REPAINT_FRAME_TIMEOUT_TM, temp_clock, sender, sender, REPAINT_FRAME_TM, "",temp,type));

                    temp2 = (int)father.getVariable(NUMSINKS);
                    temp3 = (int)father.getVariable(NUMNODES);
                    
                    for(i=0;i<temp2;i++){
                        if(getNode(temp3+i).isAlive() && (temp3+i != sender))
                            pushEvent(new event_sim(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver,temp3+i, RESET_MESSAGE_SINK, ""+temp,temp,type));
                    }
                    
                }else{
                    
                    temp = getNode(receiver).getActiveTree();
                    
                    InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock+DELTA_TIME, type,temp);
                    
                    if(getNode(receiver).isSink(temp))
                        pushEvent(new event_sim(temp_clock+NEXT_HELLO_TIMEOUT, temp_clock, receiver, receiver, SEND_HELLO_MESSAGE_TM, "",temp,type));
                    
                    
                    //If the node already received the reset message, the it will just program its next SEND_RESET_MESSAGE event.
                    pushEvent(new event_sim(temp_clock+NEXT_RESET_TIMEOUT, temp_clock, receiver,receiver, SEND_RESET_MESSAGE, "",temp,type));
                
                }
                
               
                
                break;
         
                
                case TM_DEATH_NOTIFICATION:
                    
                    if(getNode(receiver).isSink()){
                        
                        temp = getNode(receiver).getActiveTree();
                        InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock+DELTA_TIME, type,temp);
                        
                        getNode(receiver).setActiveTree(temp_tree);
                        
                        
                        //Turn off the structure of the node that sent the message
                        try{
                                temp2 = Integer.parseInt(temp_data);
                        }catch(Exception ex){ex.printStackTrace();}
                        getNode(receiver).getInfrastructure(temp2).setStarted(false);
                        
                        
                        temp = getNode(receiver).where_am_I_sink();
                        pushEvent(new event_sim(temp_clock+(PROCESSING_DELAY)+temp, temp_clock, receiver, receiver, SEND_RESET_MESSAGE, "",temp_tree,type));
                    }
                    
                    break;
                    
                
                /*
                 * *****************************************************************************
                 */
                
            case SEND_HELLO_MESSAGE_TM:
                getNode(sender).setEnergyState(STATE_READY);
                //Sends the Hello message to all its neighbors
                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, final_destination, sender, -1, HELLO_MESSAGE_TM, "",temp_tree);

                getNode(sender).setNeighbCountTM(0);
                
                //Schedule the process of counting the neighbors that answered
                pushEvent(new event_sim(temp_clock+WAIT_HELLO_REPLY_TIMEOUT, temp_clock, sender, sender, HELLO_MESSAGE_WAIT_TIMEOUT_TM, "",temp_tree,type));
                break;
                
                
                
            case HELLO_MESSAGE_TM:
                if(getNode(receiver).isActive(temp_tree)){
                    //Sends the Reply to Hello message to the sender
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, final_destination, receiver, sender, REPLY_MESSAGE_TM, "",temp_tree);
                }
                break;
            
                      
            case REPLY_MESSAGE_TM:
                if(receiver == destination){
                    getNode(receiver).add2NeighbCountTM();
                }
                break;
                
            case HELLO_MESSAGE_WAIT_TIMEOUT_TM:
                if(getNode(sender).getNeighbCountTM()==0){
                    InvalidateAllEventsFromIDFromTimeT(sender, temp_clock, type);
                    
                    temp = getNode(sender).getActiveTree();
                    
                    temp2 = (int)father.getVariable(NUMSINKS);
                    temp3 = (int)father.getVariable(NUMNODES);
                    
                    temp_data_int = getNode(sender).where_am_I_sink();
                    
                    for(i=0;i<temp2;i++){
                        if(getNode(temp3+i).isAlive() && (temp3+i != sender))
                            pushEvent(new event_sim(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver,temp3+i, TM_DEATH_NOTIFICATION, ""+temp_data_int,temp,type));
                    }
                    
                    getNode(sender).setDead();
                    father.simulation_end();
                }
                else
                    //Schedule the process of counting the neighbors that answered
                    pushEvent(new event_sim(temp_clock+NEXT_HELLO_TIMEOUT, temp_clock, sender, sender, SEND_HELLO_MESSAGE_TM, "",temp_tree,type));
                break;
            
            
            default:
                break;
        }
        }
        
    }
}
