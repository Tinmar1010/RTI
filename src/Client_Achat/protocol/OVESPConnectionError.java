package Client_Achat.protocol;

public class OVESPConnectionError extends Exception{
    public OVESPConnectionError(String errorMessage) {
        super(errorMessage);
    }
}
