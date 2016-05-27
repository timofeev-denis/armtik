package ru.voskhod.tik.exchange;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author bormotov
 */
public class Messages implements Serializable
{
    private String msgName;
    private String msgHtml;
    private ArrayList<String[]> msgList;

    public void clear()
    {
        msgName = "";
        msgHtml = "";
        if (msgList != null) msgList.clear();
    }
    /**
     * @return the msgName
     */
    public String getMsgName() 
    {
        return msgName;
    }

    /**
     * @param msgName the msgName to set
     */
    public void setMsgName(String msgName) 
    {
        this.msgName = msgName;
    }

    /**
     * @return the msgHtml
     */
    public String getMsgHtml() 
    {
        return msgHtml;
    }

    /**
     * @param msgHtml the msgHtml to set
     */
    public void setMsgHtml(String msgHtml) 
    {
        this.msgHtml = msgHtml;
    }

    /**
     * @return the msgList
     */
    public ArrayList<String[]> getMsgList() 
    {
        return msgList;
    }

    /**
     * @param msgList the msgList to set
     */
    public void setMsgList(ArrayList<String[]> msgList) 
    {
        this.msgList = msgList;
    }

}
