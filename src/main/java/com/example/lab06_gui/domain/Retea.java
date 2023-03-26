package com.example.lab06_gui.domain;

import java.util.List;
import java.util.*;

/**
 * Retea este o clasa ce lucreaza cu legaturile si comunitatile de utilizatori
 * mat - matrix with integer values
 * size - integer(size of matrix)
 * ind - Set that contains int keys
 */
public class Retea {
    private Integer[][] mat;
    private Integer size;
    private Set<Long> ind;

    /**
     * create a matrix of friends connections
     * @param size of matrix, number of users
     */
    public Retea(int size) {
        this.ind = new HashSet<>();
        this.mat = new Integer[size][size];
        this.size = size;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.mat[i][j] = 0;
    }

    /**
     * adauga prietenii in retea
     * @param list-list of Friendships
     */
    public void addPrietenii(Iterable<Prietenie> list) {
        list.forEach(f ->
                this.mat[(int) (f.getID1() - 1)][(int) (f.getID2() - 1)] = 1);
        list.forEach(f ->
                this.mat[(int) (f.getID2() - 1)][(int) (f.getID1() - 1)] = 1);

    }

    /**
     * add users to the network
     * @param list- list of Users
     */
    public void addUtilizatori(Iterable<Utilizator> list) {
        list.forEach(x -> ind.add(x.getID()-1));
    }

    /**
     * dfs algorithm
     * @param v-integer
     * @param visited-boolean array
     */
    private void DFS(int v, boolean[] visited) {
        visited[v] = true;
        for (int i = 0; i < size; i++)
            if (mat[v][i] == 1 && !visited[i])
                DFS(i, visited);
    }

    /**
     * verificam cate componente conexe avem - adica numarul de comunitati
     * @return nr - integer (rezultatul)
     */
    public int numarComunitati() {
        int nr = 0;
        boolean[] vizitat = new boolean[size];

        for (int i = 0; i < size; i++) {
            if (!vizitat[i] && ind.contains(i)) {
                DFS(i, vizitat);
                nr++;
            }
        }
        return nr;
    }

    /**
     * find the biggest Component
     * @return list of Integer(the result)
     */
    public List<Integer> biggestComponent() {
        boolean[] visited = new boolean[size];
        boolean[] viz=new boolean[size];
        List<Integer>listFinal=new ArrayList<>();
        int maxim=0;
        for (int i = 0; i < size; i++)
            if (!visited[i] && ind.contains((int) i)) {
                DFS(i, visited);
                int nr=0;
                List<Integer>listForNow=new ArrayList<>();
                for(int p=0;p<size;p++){
                    if(visited[p]!=viz[p]){
                        nr++;
                        listForNow.add((int) p + 1);
                        viz[p]=visited[p];
                    }
                    if(nr>maxim){
                        listFinal.clear();
                        listFinal.addAll(listForNow);
                        maxim=nr;
                    }
                }
            }
        return listFinal;
    }
}