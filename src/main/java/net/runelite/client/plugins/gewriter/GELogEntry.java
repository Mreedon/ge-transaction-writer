package net.runelite.client.plugins.gewriter;

import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.client.game.ItemManager;

public class GELogEntry
{
    public String eventType;
    public String itemName;
    public int quantityOffered;
    public int quantityFilled;
    public int price;
    public int slot;
    public long timestamp;
    public String state;

    public static GELogEntry fromEvent(GrandExchangeOfferChanged event, ItemManager itemManager, String eventType)
    {
        GELogEntry entry = new GELogEntry();

        entry.eventType = eventType;

        if (event.getOffer().getState() == GrandExchangeOfferState.EMPTY)
        {
            entry.itemName = "EMPTY";
        }
        else
        {
            entry.itemName = itemManager.getItemComposition(event.getOffer().getItemId()).getName();
        }

        entry.quantityOffered = event.getOffer().getTotalQuantity();
        entry.quantityFilled = event.getOffer().getQuantitySold();
        entry.price = event.getOffer().getPrice();
        entry.slot = event.getSlot();
        entry.timestamp = System.currentTimeMillis();
        entry.state = event.getOffer().getState().name();

        return entry;
    }
}
