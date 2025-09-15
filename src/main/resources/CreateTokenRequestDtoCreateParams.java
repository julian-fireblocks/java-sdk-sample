public class CreateTokenRequestDtoCreateParams {
    private String name;
    private String symbol;
    private int decimals;
    private String initialSupply;
    private String contractType; // e.g., ERC20, ERC721, etc.

    public CreateTokenRequestDtoCreateParams(String name, String symbol, int decimals, String initialSupply, String contractType) {
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.initialSupply = initialSupply;
        this.contractType = contractType;
    }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public int getDecimals() { return decimals; }
    public String getInitialSupply() { return initialSupply; }
    public String getContractType() { return contractType; }

    public void setName(String name) { this.name = name; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setDecimals(int decimals) { this.decimals = decimals; }
    public void setInitialSupply(String initialSupply) { this.initialSupply = initialSupply; }
    public void setContractType(String contractType) { this.contractType = contractType; }
}

