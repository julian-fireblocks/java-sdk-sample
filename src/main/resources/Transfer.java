import com.fireblocks.sdk.ApiResponse;
import com.fireblocks.sdk.ConfigurationOptions;
import com.fireblocks.sdk.Fireblocks;
import com.fireblocks.sdk.model.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Transfer {
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
            
            ConfigurationOptions options = new ConfigurationOptions();
            options.apiKey(apiKey);
            options.secretKey(privateKey);
            options.basePath("https://api.fireblocks.io/v1");
                    
            Fireblocks fireblocksSDK = new Fireblocks(options);

            // Define transfer parameters (replace with actual values)
            String assetId = "ETH_TEST6"; // e.g., "ETH", "USDC"
            String sourceVaultAccountId = "0";
            String destinationVaultAccountId = "1";
            String amount = "0.001"; // Amount as float
            String note = "Test transfer";


            TransactionRequest tr = new TransactionRequest();
            // Create the transaction (transfer) using the Fireblocks SDK
            // The actual method signature may differ depending on SDK version

            TransactionRequestAmount tra = new TransactionRequestAmount(amount);
            tr.setAmount(tra);
            tr.setAssetId(assetId);

            DestinationTransferPeerPath dest = new DestinationTransferPeerPath(TransferPeerPathType.VAULT_ACCOUNT);
            dest.setId(destinationVaultAccountId);

            SourceTransferPeerPath source = new SourceTransferPeerPath(TransferPeerPathType.VAULT_ACCOUNT);
            source.setId(sourceVaultAccountId);

            tr.setDestination(dest);
            tr.setSource(source);

            CompletableFuture<ApiResponse<CreateTransactionResponse>> response = fireblocksSDK.transactions().createTransaction(
                tr,
                null,
                UUID.randomUUID().toString()
            );

            String result = response.get().getData().getStatus();
            // Print the response
            System.out.println("Transfer response: " + result);
        } catch (Exception e) {
            System.err.println("Error performing transfer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

