package com.rarecandy.rarecandyapi.database;

import com.rarecandy.rarecandyapi.RareCandyPlugin;
import com.rarecandy.rarecandyapi.data.PlayerData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private static HikariDataSource dataSource;

    public static void init(RareCandyPlugin plugin) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + plugin.getConfig().getString("database.host", "localhost") + ":" +
                plugin.getConfig().getInt("database.port", 3306) + "/" +
                plugin.getConfig().getString("database.database", "rarecandy"));
        config.setUsername(plugin.getConfig().getString("database.username", "root"));
        config.setPassword(plugin.getConfig().getString("database.password", ""));

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
        createTable();
    }

    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    private static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS rarecandy_stats (" +
                "uuid VARCHAR(36) PRIMARY KEY, level INT, xp INT, totalCatches INT, shinyCatches INT, " +
                "bossDefeats INT, wildDefeats INT, npcWins INT, eggsHatched INT, evolutions INT, " +
                "raidWins INT, raidCaptures INT, tradesCompleted INT, legendariesCaught INT, " +
                "ultraBeastsCaught INT, pokestopsSpun INT, pokeLootsClaimed INT, apricornsPicked INT, " +
                "successfulFishes INT, pokemonLeveledUp INT, megasUsed INT, dynamaxUsed INT, " +
                "fossilsRestored INT, eggsObtained INT, shrinesActivated INT, zMovesUsed INT, " +
                "mythicalsCaught INT, prestigeLevel INT, evolutionStonesUsed INT, prestigeTokens INT, " +
                "lifetimeCatches INT, lifetimeLegendaries INT, lifetimeBossDefeats INT" +
                ");";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to create database table!");
            e.printStackTrace();
        }
    }

    public static CompletableFuture<PlayerData> loadPlayerAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerData data = new PlayerData(uuid);
            String sql = "SELECT * FROM rarecandy_stats WHERE uuid = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, uuid.toString());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        data.setLevel(rs.getInt("level"));
                        data.setXp(rs.getInt("xp"));
                        data.setTotalCatches(rs.getInt("totalCatches"));
                        data.setShinyCatches(rs.getInt("shinyCatches"));
                        data.setBossDefeats(rs.getInt("bossDefeats"));
                        data.setWildDefeats(rs.getInt("wildDefeats"));
                        data.setNpcWins(rs.getInt("npcWins"));
                        data.setEggsHatched(rs.getInt("eggsHatched"));
                        data.setEvolutions(rs.getInt("evolutions"));
                        data.setRaidWins(rs.getInt("raidWins"));
                        data.setRaidCaptures(rs.getInt("raidCaptures"));
                        data.setTradesCompleted(rs.getInt("tradesCompleted"));
                        data.setLegendariesCaught(rs.getInt("legendariesCaught"));
                        data.setUltraBeastsCaught(rs.getInt("ultraBeastsCaught"));
                        data.setPokestopsSpun(rs.getInt("pokestopsSpun"));
                        data.setPokeLootsClaimed(rs.getInt("pokeLootsClaimed"));
                        data.setApricornsPicked(rs.getInt("apricornsPicked"));
                        data.setSuccessfulFishes(rs.getInt("successfulFishes"));
                        data.setPokemonLeveledUp(rs.getInt("pokemonLeveledUp"));
                        data.setMegasUsed(rs.getInt("megasUsed"));
                        data.setDynamaxUsed(rs.getInt("dynamaxUsed"));
                        data.setFossilsRestored(rs.getInt("fossilsRestored"));
                        data.setEggsObtained(rs.getInt("eggsObtained"));
                        data.setShrinesActivated(rs.getInt("shrinesActivated"));
                        data.setZMovesUsed(rs.getInt("zMovesUsed"));
                        data.setMythicalsCaught(rs.getInt("mythicalsCaught"));
                        data.setPrestigeLevel(rs.getInt("prestigeLevel"));
                        data.setEvolutionStonesUsed(rs.getInt("evolutionStonesUsed"));
                        data.setPrestigeTokens(rs.getInt("prestigeTokens"));
                        data.setLifetimeCatches(rs.getInt("lifetimeCatches"));
                        data.setLifetimeLegendaries(rs.getInt("lifetimeLegendaries"));
                        data.setLifetimeBossDefeats(rs.getInt("lifetimeBossDefeats"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data;
        });
    }

    public static CompletableFuture<Void> savePlayerAsync(PlayerData data) {
        return CompletableFuture.runAsync(() -> {
            String sql = "INSERT INTO rarecandy_stats (uuid, level, xp, totalCatches, shinyCatches, bossDefeats, wildDefeats, npcWins, eggsHatched, evolutions, raidWins, raidCaptures, tradesCompleted, legendariesCaught, ultraBeastsCaught, pokestopsSpun, pokeLootsClaimed, apricornsPicked, successfulFishes, pokemonLeveledUp, megasUsed, dynamaxUsed, fossilsRestored, eggsObtained, shrinesActivated, zMovesUsed, mythicalsCaught, prestigeLevel, evolutionStonesUsed, prestigeTokens, lifetimeCatches, lifetimeLegendaries, lifetimeBossDefeats) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "level=VALUES(level), xp=VALUES(xp), totalCatches=VALUES(totalCatches), shinyCatches=VALUES(shinyCatches), " +
                    "bossDefeats=VALUES(bossDefeats), wildDefeats=VALUES(wildDefeats), npcWins=VALUES(npcWins), " +
                    "eggsHatched=VALUES(eggsHatched), evolutions=VALUES(evolutions), raidWins=VALUES(raidWins), " +
                    "raidCaptures=VALUES(raidCaptures), tradesCompleted=VALUES(tradesCompleted), " +
                    "legendariesCaught=VALUES(legendariesCaught), ultraBeastsCaught=VALUES(ultraBeastsCaught), " +
                    "pokestopsSpun=VALUES(pokestopsSpun), pokeLootsClaimed=VALUES(pokeLootsClaimed), " +
                    "apricornsPicked=VALUES(apricornsPicked), successfulFishes=VALUES(successfulFishes), " +
                    "pokemonLeveledUp=VALUES(pokemonLeveledUp), megasUsed=VALUES(megasUsed), " +
                    "dynamaxUsed=VALUES(dynamaxUsed), fossilsRestored=VALUES(fossilsRestored), " +
                    "eggsObtained=VALUES(eggsObtained), shrinesActivated=VALUES(shrinesActivated), " +
                    "zMovesUsed=VALUES(zMovesUsed), mythicalsCaught=VALUES(mythicalsCaught), " +
                    "prestigeLevel=VALUES(prestigeLevel), evolutionStonesUsed=VALUES(evolutionStonesUsed), " +
                    "prestigeTokens=VALUES(prestigeTokens), lifetimeCatches=VALUES(lifetimeCatches), " +
                    "lifetimeLegendaries=VALUES(lifetimeLegendaries), lifetimeBossDefeats=VALUES(lifetimeBossDefeats)";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, data.getUuid().toString());
                stmt.setInt(2, data.getLevel());
                stmt.setInt(3, data.getXp());
                stmt.setInt(4, data.getTotalCatches());
                stmt.setInt(5, data.getShinyCatches());
                stmt.setInt(6, data.getBossDefeats());
                stmt.setInt(7, data.getWildDefeats());
                stmt.setInt(8, data.getNpcWins());
                stmt.setInt(9, data.getEggsHatched());
                stmt.setInt(10, data.getEvolutions());
                stmt.setInt(11, data.getRaidWins());
                stmt.setInt(12, data.getRaidCaptures());
                stmt.setInt(13, data.getTradesCompleted());
                stmt.setInt(14, data.getLegendariesCaught());
                stmt.setInt(15, data.getUltraBeastsCaught());
                stmt.setInt(16, data.getPokestopsSpun());
                stmt.setInt(17, data.getPokeLootsClaimed());
                stmt.setInt(18, data.getApricornsPicked());
                stmt.setInt(19, data.getSuccessfulFishes());
                stmt.setInt(20, data.getPokemonLeveledUp());
                stmt.setInt(21, data.getMegasUsed());
                stmt.setInt(22, data.getDynamaxUsed());
                stmt.setInt(23, data.getFossilsRestored());
                stmt.setInt(24, data.getEggsObtained());
                stmt.setInt(25, data.getShrinesActivated());
                stmt.setInt(26, data.getZMovesUsed());
                stmt.setInt(27, data.getMythicalsCaught());
                stmt.setInt(28, data.getPrestigeLevel());
                stmt.setInt(29, data.getEvolutionStonesUsed());
                stmt.setInt(30, data.getPrestigeTokens());
                stmt.setInt(31, data.getLifetimeCatches());
                stmt.setInt(32, data.getLifetimeLegendaries());
                stmt.setInt(33, data.getLifetimeBossDefeats());

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}