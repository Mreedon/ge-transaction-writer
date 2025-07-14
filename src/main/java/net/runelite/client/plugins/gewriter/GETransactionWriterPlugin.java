package net.runelite.client.plugins.gewriter;

import com.google.inject.Provides;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@PluginDescriptor(
        name = "GE Transaction Writer",
        description = "Logs GE transactions to a local JSON file for analysis and tracking",
        tags = {"ge", "flipping", "tracker", "log"}
)
public class GETransactionWriterPlugin extends Plugin
{
    private static final Gson GSON = new GsonBuilder().create();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final Path logDir = Path.of(System.getProperty("user.home"), ".runelite", "getransactionwriter");

    @Inject
    private GETransactionWriterConfig config;

    @Inject
    private ItemManager itemManager;

    @Subscribe
    public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged event)
    {
        String eventType = determineEventType(event);
        GELogEntry entry = GELogEntry.fromEvent(event, itemManager, eventType);

        String fileName = "transactions-" + DATE_FORMAT.format(new Date()) + ".json";
        Path filePath = logDir.resolve(fileName);

        try
        {
            logDir.toFile().mkdirs();
            try (FileWriter writer = new FileWriter(filePath.toFile(), true))
            {
                writer.write(GSON.toJson(entry));
                writer.write("\n");
            }
        }
        catch (IOException e)
        {
            log.error("Failed to write GE log entry", e);
        }
    }

    private String determineEventType(GrandExchangeOfferChanged event)
    {
        GrandExchangeOfferState state = event.getOffer().getState();

        if (state == GrandExchangeOfferState.EMPTY)
        {
            return "EMPTY_SLOT";
        }

        switch (state)
        {
            case BUYING:
            case BOUGHT:
                return "BUY";
            case SELLING:
            case SOLD:
                return "SELL";
            case CANCELLED_BUY:
                return "CANCELLED_BUY";
            case CANCELLED_SELL:
                return "CANCELLED_SELL";
            default:
                return "UNKNOWN";
        }
    }

    @Provides
    GETransactionWriterConfig provideConfig(com.google.inject.Injector injector)
    {
        return injector.getInstance(GETransactionWriterConfig.class);
    }
}
