package com.vengeance.game.database;

import com.vengeance.game.entity.Entity;
import com.vengeance.game.main.GamePanel;
import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    private Connection connection;
    private Statement stmt;
    GamePanel gp;

    public DataBase(GamePanel gp) {
        this.gp = gp;
    }
    public void createPlayerTable(String tableName, ArrayList<String> fields) {
        connection = null;
        stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = connection.createStatement();
            String sql = "CREATE TABLE if NOT EXISTS " + tableName + " (";
            for (int i = 0; i < fields.size(); i += 2) {
                sql += fields.get(i) + " " + fields.get(i + 1) + " NOT NULL, ";
            }
            sql = sql.substring(0, sql.length() - 2);
            sql += ");";
            stmt.execute(sql);
            stmt.close();
            connection.close();
            System.out.println("Tabelul " + tableName + " a fost creat cu succes.");
        } catch (ClassNotFoundException e) {
            System.out.println("Eroare: Instalati driverul SQLite.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Eroare: Eroare la crearea tabelului.");
            e.printStackTrace();
        }
    }
    public void insertPlayerTable(String tableName, ArrayList<String> fields, ArrayList<String> values) {
        connection = null;
        stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            String sql = "INSERT OR IGNORE INTO " + tableName + " (";
            for(int i = 0; i < fields.size(); ++i) {
                sql += "\"" + fields.get(i) + "\"" + ", ";
            }
            sql = sql.substring(0, sql.length() - 2);
            sql += ") VALUES (";
            for (int i = 0; i < values.size(); ++i) {
                sql += "\"" + values.get(i) + "\"" + ", ";
            }
            sql = sql.substring(0, sql.length() - 2);
            sql += "); UPDATE " + tableName + " SET ";
            for(int i = 0; i < fields.size(); ++i) {
                sql += "\"" + fields.get(i) + "\"" + "=" + "\"" + values.get(i) + "\"" + ", ";
            }

            sql = sql.substring(0, sql.length() - 2);
            stmt.executeUpdate(sql);
            connection.commit();
            stmt.close();
            connection.close();
            System.out.println("Update la tabelul " + tableName + " cu succes.");
        } catch (ClassNotFoundException e) {
            System.out.println("Eroare: Class not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Eroare: Eroare la inserarea in tabel.");
            e.printStackTrace();
        }
    }

    public void selectPlayerTable(String tableName) {
        connection = null;
        stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");

            // Variables
            int playerX = 0;
            int playerY = 0;
            int currentMap = 1;
            String direction = "";
            int life = 0;
            int keys = 0;
            String enemies = "";
            String npc = "";
            String objects = "";

            while(rs.next()) {
                playerX = Integer.parseInt(rs.getString("PLAYERX"));
                playerY = Integer.parseInt(rs.getString("PLAYERY"));
                currentMap = rs.getInt("CURRENTMAP");
                direction = rs.getString("DIRECTION");
                life = rs.getInt("LIFE");
                keys = rs.getInt("KEYS");
                enemies = rs.getString("ENEMIES");
                npc = rs.getString("NPC");
                objects = rs.getString("OBJECTS");
            }
            gp.player.worldX = playerX;
            gp.player.worldY = playerY;
            gp.currentMap = currentMap;
            gp.player.walkDirection = gp.player.getDirectionFromString(direction);
            gp.player.life = life;
            gp.player.keysGathered = keys;

            // Parse
            ArrayList<Entity> restoredEntities = new ArrayList<>();
            String[] enemy = enemies.split(", ");
            int k, map, index;
            for (int i = 0; i < enemy.length/5; i++) {
                k = i * 5;
                map = Integer.parseInt(enemy[k]);
                index = Integer.parseInt(enemy[k+1]);
                restoredEntities.add(gp.enemy[map][index]);
                gp.enemy[map][index].worldX = Integer.parseInt(enemy[k + 2]);
                gp.enemy[map][index].worldY = Integer.parseInt(enemy[k + 3]);
                gp.enemy[map][index].life = Integer.parseInt(enemy[k + 4]);
            }
            deleteDeadEntity(restoredEntities, gp.enemy);

//            int k = 0;
//            for(int i = 0; i < gp.enemy.length; ++i) {
//                for(int j = 0; j < gp.enemy[i].length; ++j) {
//                    if(gp.enemy[i][j] != null) {
//                        int map = Integer.parseInt(arrayMonsters[k]);
//                        int index = Integer.parseInt(arrayMonsters[k + 1]);
//                        gp.enemy[i][j].worldX = Integer.parseInt(arrayMonsters[k + 2]);
//                        gp.enemy[i][j].worldY = Integer.parseInt(arrayMonsters[k + 3]);
//                        gp.enemy[i][j].life = Integer.parseInt(arrayMonsters[k + 4]);
//                    }
//                    k += 3;
//                }
//            }
            String[] arrayNPC = npc.split(", ");
            for (int i = 0; i < arrayNPC.length/4; i++) {
                k = i * 4;
                map = Integer.parseInt(arrayNPC[k]);
                index = Integer.parseInt(arrayNPC[k+1]);
                restoredEntities.add(gp.npc[map][index]);
                gp.npc[map][index].worldX = Integer.parseInt(arrayNPC[k + 2]);
                gp.npc[map][index].worldY = Integer.parseInt(arrayNPC[k + 3]);
            }
            deleteDeadEntity(restoredEntities, gp.npc);

//            k = 0;
//            for(int i = 0; i < gp.npc.length; ++i) {
//                for(int j = 0; j < gp.npc[i].length; ++j) {
//                    if(gp.npc[i][j] != null) {
//                        gp.npc[i][j].worldX = Integer.parseInt(arrayNPC[k]);
//                        gp.npc[i][j].worldY = Integer.parseInt(arrayNPC[k + 1]);
//                    }
//                    k += 2;
//                }
//            }
            String[] arrayObjects = objects.split(", ");
            for (int i = 0; i < arrayObjects.length/4; i++) {
                k = i * 4;
                map = Integer.parseInt(arrayObjects[k]);
                index = Integer.parseInt(arrayObjects[k+1]);
                restoredEntities.add(gp.obj[map][index]);
                gp.obj[map][index].worldX = Integer.parseInt(arrayObjects[k + 2]);
                gp.obj[map][index].worldY = Integer.parseInt(arrayObjects[k + 3]);
            }
            deleteDeadEntity(restoredEntities, gp.obj);


//            k = 0;
//            for(int i = 0; i < gp.obj.length; ++i) {
//                for(int j = 0; j < gp.obj[i].length; ++j) {
//                    if(gp.obj[i][j] != null) {
//                        gp.obj[i][j].worldX = Integer.parseInt(arrayObjects[k]);
//                        gp.obj[i][j].worldY = Integer.parseInt(arrayObjects[k + 1]);
//                    }
//                    k += 2;
//                }
//            }
            rs.close();
            stmt.close();
            connection.close();
        } catch(ClassNotFoundException e) {
            System.out.println("Eroare: Class not found.");
            e.printStackTrace();
        } catch(SQLException e) {
            System.out.println("Eroare: Eroare la extragerea datelor din tabel.");
            e.printStackTrace();
        }
    }

    private void deleteDeadEntity(ArrayList<Entity> restoredEntities, Entity[][] entityMatrix) {
        // Reset the entities that were not in the database, which means they were defeated by us
        for (int i = 0; i < gp.maxMap; i++) {
            for (int j = 0; j < entityMatrix[i].length; j++) {
                if (entityMatrix[i][j] != null && !restoredEntities.contains(entityMatrix[i][j])) {
                    entityMatrix[i][j] = null;
                }
            }
        }
        restoredEntities.clear();
    }

    public void saveData() {
        // Entities
        String enemies = "";
        for(int map = 0; map < gp.maxMap; ++map) {
            for(int j = 0; j < gp.enemy[map].length; ++j) {
                if(gp.enemy[map][j] != null) {
                    enemies += map + ", " + j + ", " + gp.enemy[map][j].worldX + ", " + gp.enemy[map][j].worldY + ", " + gp.enemy[map][j].life + ", ";
                }
            }
        }
        if(!enemies.isEmpty()) {
            enemies = enemies.substring(0, enemies.length() - 2);
        }

        String NPC = "";
        for(int i = 0; i < gp.maxMap; ++i) {
            for(int j = 0; j < gp.npc[i].length; ++j) {
                if(gp.npc[i][j] != null) {
                    NPC += i + ", " + j + ", " + gp.npc[i][j].worldX + ", " + gp.npc[i][j].worldY + ", ";
                }
            }
        }
        if(!NPC.isEmpty()) {
            NPC = NPC.substring(0, NPC.length() - 2);
        }

        String objects = "";
        for(int i = 0; i < gp.maxMap; ++i) {
            for(int j = 0; j < gp.obj[i].length; ++j) {
                if(gp.obj[i][j] != null) {
                    objects += i + ", " + j + ", " +  gp.obj[i][j].worldX + ", " + gp.obj[i][j].worldY + ", ";
                }
            }
        }
        if(!objects.isEmpty()) {
            objects = objects.substring(0, objects.length() - 2);
        }

        // Player Settings
        String tableName = "STATS";

        // Creare tabel daca nu exista
        ArrayList<String> fields = new ArrayList<>();
        fields.add("PLAYERX");
        fields.add("TEXT");
        fields.add("PLAYERY");
        fields.add("TEXT");
        fields.add("CURRENTMAP");
        fields.add("TEXT");
        fields.add("DIRECTION");
        fields.add("TEXT");
        fields.add("LIFE");
        fields.add("TEXT");
        fields.add("KEYS");
        fields.add("TEXT");
        fields.add("ENEMIES");
        fields.add("TEXT");
        fields.add("NPC");
        fields.add("TEXT");
        fields.add("OBJECTS");
        fields.add("TEXT");
        createPlayerTable(tableName, fields);

        // Adaugare date in tabel
        fields.clear();
        fields.add("PLAYERX"); // 1
        fields.add("PLAYERY"); // 2
        fields.add("CURRENTMAP"); // 3
        fields.add("DIRECTION"); // 4
        fields.add("LIFE"); // 5
        fields.add("KEYS"); // 6
        fields.add("ENEMIES"); // 7
        fields.add("NPC"); // 8
        fields.add("OBJECTS"); // 9
        ArrayList<String> playerData = getStrings(enemies, NPC, objects);
        insertPlayerTable(tableName, fields, playerData);
    }

    private ArrayList<String> getStrings(String monsters, String NPC, String objects) {
        ArrayList<String> playerData = new ArrayList<>();
        playerData.add(String.valueOf(gp.player.worldX)); // 1
        playerData.add(String.valueOf(gp.player.worldY)); // 2
        playerData.add(String.valueOf(gp.currentMap)); // 3
        playerData.add(gp.player.getStringFromDirection()); // 4
        playerData.add(String.valueOf(gp.player.life)); // 5
        playerData.add(String.valueOf(gp.player.keysGathered)); // 6
        playerData.add(monsters); // 7
        playerData.add(NPC); // 8
        playerData.add(objects); // 9
        return playerData;
    }
}