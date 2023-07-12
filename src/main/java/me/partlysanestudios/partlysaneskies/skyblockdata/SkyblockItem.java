//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.skyblockdata;

import java.util.ArrayList;
import java.util.Comparator;

public class SkyblockItem {
    private String id;
    private String name;
    private String rarity;
    private double npcSellPrice;
    private double bazaarSellPrice;


    private double bazaarBuyPrice;
    private double lowestBinPrice;
    private double averageLowestBinPrice;
    private int bitCost;

    public SkyblockItem(String id, String name, double npcSellPrice, String rarity) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.npcSellPrice = npcSellPrice;
        this.bazaarSellPrice = -1;
        this.lowestBinPrice = -1;
        this.averageLowestBinPrice = -1;
        this.bitCost = -1;
    }


    public SkyblockItem setBazaarSellPrice(double price) {
        this.bazaarSellPrice = price;
        return this;
    }

    public void setBazaarBuyPrice(double bazaarBuyPrice) {
        this.bazaarBuyPrice = bazaarBuyPrice;
    }


    public SkyblockItem setLowestBinPrice(double price) {
        this.lowestBinPrice = price;

        return this;
    }
    public SkyblockItem setBitCost(int bitCost) {
        this.bitCost = bitCost;

        return this;
    }

    public SkyblockItem setAverageLowestBinPrice(double price) {
        this.averageLowestBinPrice = price;
        return this;
    }
    
    public int getBitCost() {
        return this.bitCost;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRarity() {
        return rarity;
    }

    public double getSellPrice() {
        if (getBazaarSellPrice() != -1) {
            return getBazaarSellPrice();
        }
        if (getLowestBin() != -1) {
            return getLowestBin();
        }
        if (getNpcSellPrice() != -1) {
            return getAverageLowestBin();
        }
        return -1;
    }

    public double getBuyPrice() {
        if (getBazaarBuyPrice() != -1) {
            return getBazaarSellPrice();
        }
        if (getLowestBin() != -1) {
            return getLowestBin();
        }
        return -1;
    }

    public double getBestPrice() {
        ArrayList<Double> list = new ArrayList<>();
        list.add(getBazaarSellPrice());
        list.add(getLowestBin());
        list.add(getNpcSellPrice());

        list.sort(Comparator.naturalOrder());

        return list.get(list.size() - 1);
    }

    public double getLowestBin() {
        return this.lowestBinPrice;
    }

    public double getNpcSellPrice() {
        return this.npcSellPrice;
    }

    public double getBazaarSellPrice() {
        return this.bazaarSellPrice;
    }

    public double getBazaarBuyPrice() {
        return bazaarBuyPrice;
    }

    public double getAverageLowestBin() {
        if (this.averageLowestBinPrice != -1) {
            return this.averageLowestBinPrice;
        }
        return -1;
    }

    public boolean hasAverageLowestBin() {
        if (this.averageLowestBinPrice == -1) {
            return false;
        }
        return true;
    }

    public boolean hasSellPrice() {
        return getSellPrice() != -1;
    }

    public boolean hasBuyPrice() {
        return getBuyPrice() != -1;
    }

    public boolean hasBitCost() {
        return bitCost != -1;
    }

    public String getRarityColorCode() {
        switch (rarity) {
            case "COMMON":
                return "§f";

            case "UNCOMMON":
                return "§a";

            case "RARE":
                return "§9";

            case "EPIC":
                return "§5";

            case "LEGENDARY":
                return "§6";

            case "DIVINE":
                return "§b";

            case "MYTHIC":
                return "§d";

            case "SUPREME":
                return "§4";

            case "SPECIAL":
            case "VERY_SPECIAL":
                return "§c";

            default:
                return "";
        }
    }
}
