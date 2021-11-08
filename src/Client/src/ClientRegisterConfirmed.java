/**
 * class for response from server when the registrations is confirmed
 */
public class ClientRegisterConfirmed extends Request  {

    private static final long serialVersionUID = 1L;

    //constructors
    public ClientRegisterConfirmed() {
        super(RequestType.CLIENT_REGISTER_CONFIRMED);
    }

    public ClientRegisterConfirmed(int RQNumb){
        super(RequestType.CLIENT_REGISTER_CONFIRMED);
        this.RQNumb = RQNumb;
    }

    /**
     * Get a string of the request
     * @return
     */
    @Override
    public String toString(){
        return RequestType.CLIENT_REGISTER_CONFIRMED+ " " + this.getRQNumb()+ " " ;
    }
}
