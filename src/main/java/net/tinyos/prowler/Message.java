package net.tinyos.prowler;

public class Message {
    int seqId;
    String content;

    int ttl;

    public Message(int seqId, String content, int ttl){
        this.content = content;
        this.seqId = seqId;
        this.ttl = ttl;
    }

    int getSeqId(){
        return seqId;
    }
    int getTtl(){
        return ttl;
    }
    String getContent(){
        return content;
    }


}
