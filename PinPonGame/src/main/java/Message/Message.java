package Message;
/**
 * Bilgisayar Aglari Proje 1
 * @author Habibe Gurel 1921221034
 */
public class Message implements java.io.Serializable {

    public static enum Message_Type {None, ServerCome,RivalFound,PaddleUp,PaddleDown,PaddleStop,ChangeScore}

    public Message_Type type;
    public Object content;

    public Message(Message_Type t) {
        this.type = t;
    }

}
