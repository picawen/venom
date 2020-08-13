package com.fuxw.venom.common.entity;

/**
 * 传输消息体
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 14:16
 **/
public class Message {
    /**
     * 消息ID
     */
    private String id;
    /**
     * 消息名称
     */
    private String name;
    /**
     * 消息内容
     */
    private String message;

    public Message() {
    }

    public Message(String id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
