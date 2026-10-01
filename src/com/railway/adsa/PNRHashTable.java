package com.railway.adsa;

import java.util.HashMap;

public class PNRHashTable {

    private HashMap<Long, Integer> pnrTable;

    public PNRHashTable() {
        pnrTable = new HashMap<Long, Integer>();
    }

    /*
     * Insert PNR and corresponding booking ID.
     */
    public void insert(long pnr, int bookingId) {

        pnrTable.put(pnr, bookingId);
    }

    /*
     * Search for a PNR.
     */
    public int search(long pnr) {

        if (pnrTable.containsKey(pnr)) {

            return pnrTable.get(pnr);
        }

        return -1;
    }

    /*
     * Delete a PNR.
     */
    public void delete(long pnr) {

        pnrTable.remove(pnr);
    }

    /*
     * Check whether PNR exists.
     */
    public boolean contains(long pnr) {

        return pnrTable.containsKey(pnr);
    }

    /*
     * Get number of stored PNRs.
     */
    public int size() {

        return pnrTable.size();
    }
}