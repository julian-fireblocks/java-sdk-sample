import com.fireblocks.sdk.Fireblocks;
import CreateTokenRequestDto;
import CreateTokenRequestDtoCreateParams;
import java.nio.file.Files;
import java.nio.file.Paths;


public class RunTokenDeploy {
    public static void main(String[] args) {
        try {
            // Read API key and private key path from environment variables
            String apiKey = System.getenv("FIREBLOCKS_API_KEY");
            String privateKeyPath = System.getenv("FIREBLOCKS_PRIVATE_KEY_PATH");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalArgumentException("FIREBLOCKS_API_KEY environment variable is not set.");
            }
            if (privateKeyPath == null || privateKeyPath.isEmpty()) {
                // Default for local development
                privateKeyPath = "src/main/resources/fireblocks_private.key";
            }
            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
            Fireblocks fireblocksSDK = new Fireblocks(apiKey, privateKey);

            // Define parameters for token deployment
            String vaultAccountId = "<YOUR_VAULT_ACCOUNT_ID>"; // Replace with your vault account ID
            String name = "MyToken";
            String symbol = "MTK";
            int decimals = 18;
            String initialSupply = "1000000"; // As string for large numbers
            String contractType = "ERC20";
            String blockchainId = "ETHEREUM";

            // Create the createParams object
            CreateTokenRequestDtoCreateParams createParams = new CreateTokenRequestDtoCreateParams(
                name, symbol, decimals, initialSupply, contractType
            );

            // Construct the token request DTO
            CreateTokenRequestDto tokenRequestDto = new CreateTokenRequestDto(vaultAccountId, createParams)
                .blockchainId(blockchainId)
                .displayName(name)
                .assetId(symbol);

            // Deploy the token contract
            String response = fireblocksSDK.tokenization().createToken(tokenRequestDto);

            // Print the response
            System.out.println("Token deployment response: " + response);
        } catch (Exception e) {
            System.err.println("Error deploying token: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
