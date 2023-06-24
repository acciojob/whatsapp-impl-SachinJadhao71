package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception{
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }
        else{
            userMobile.add(mobile);
            return "SUCCESS";
        }
    }

    int idx = 1;
    public Group createGroup(List<User> users) {
        Group g = new Group("raj",2);
        groupUserMap.put(g,users);
        int size = users.size();

        if(size==2){
            User u = users.get(size-1);
            adminMap.put(g,u);

            g.setName(u.getName());
            g.setNumberOfParticipants(size);
        }
        else{
            g.setName("Group " + idx);
            idx++;
            g.setNumberOfParticipants(size);
        }

        return g;
    }

    public int createMessage(String content) {
        Date date = new Date("22/03/04");
        Message m = new Message(this.messageId,content,date);
        m.setId(m.getId()+1);
        m.setContent(content);

        return m.getId();
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(! groupMessageMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        else if(!groupUserMap.containsKey(sender)){
            throw new Exception("You are not allowed to send message");
        }
        else{
            return groupMessageMap.get(group).size();
        }
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.

        if(!adminMap.containsKey(group)){
           throw new Exception("Group does not exist");
        }
        List<User> al = groupUserMap.get(group);
        boolean flag = false;

        for(User u : al){
            if(approver == u){
                flag = true;
                break;
            }
        }

        if(flag==false){
            throw new Exception("Approver does not have rights");
        }
        if(flag==false){
            throw new Exception("User is not a participant");
        }

        adminMap.put(group,user);

        return "SUCCESS";


    }

    public int removeUser(User user) throws Exception{
        //This is a bonus problem and does not contains any marks
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)
        Group g = new Group("sachin",2);
        List<User> al = groupUserMap.get(g);
        boolean flag = false;

        for(User u : al){
            if(u==user){
                flag=true;
                groupUserMap.remove(user);
            }
        }
        if(!adminMap.containsKey(user)){
            throw new Exception("Cannot remove admin");
        }
        if(flag==false){
            throw new Exception("User not found");
        }
        senderMap.remove(user);

        return groupUserMap.get(g).size();
    }

    public String findMessage(Date start, Date end, int k) throws Exception{
        if(senderMap.size() < k){
            throw new Exception("K is greater than the number of messages");
        }
        return "SUCCESS";
    }
}
