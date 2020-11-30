package ripple.server.simulation.core.expander;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/***************************************************************************
 *
 * @author qingzhou.sjq
 * @date 2019/1/8
 *
 ***************************************************************************/
public class NetBuilder {
    public static void main(String[] args) throws IOException {
        int edgesPerRound = 30;
        int totalNodeNumber = 10000;


        PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.degree > o2.degree) {
                    return 1;
                } else if (o1.degree < o2.degree) {
                    return -1;
                } else {
                    if (o1.id > o2.id) {
                        return 1;
                    } else if (o1.id < o2.id) {
                        return -1;
                    }
                }
                return 0;
            }
        });


        int currentId = 1;
        List<Edge> edgeList = new ArrayList<>();


        // 初始独立点
        for (int i = 0; i < edgesPerRound; i++) {
            queue.add(new Node(currentId, 0));
            currentId++;
        }

//        // 初始完全图
//        for (int i = 0; i < edgesPerRound; i++) {
//            queue.add(new Node(currentId, edgesPerRound - 1));
//            currentId++;
//        }
//
//        for (int i = 1; i <= edgesPerRound; i++) {
//            for (int j = 1; j <= edgesPerRound; j++) {
//                if (i != j) {
//                    edgeList.add(new Edge(i, j));
//                }
//            }
//        }


        List<Node> tmpNodeList = new ArrayList<>(edgesPerRound);

        for (int i = 0; i < totalNodeNumber - edgesPerRound; i++) {

            // 找出度数最小序号也最小的几个点
            for (int j = 0; j < edgesPerRound; j++) {
                Node node = queue.poll();
                // 添加边
                node.degree += 1;
                tmpNodeList.add(node);
                edgeList.add(new Edge(node.id, currentId));
                edgeList.add(new Edge(currentId, node.id));
            }
            queue.addAll(tmpNodeList);

            // 添加新点
            queue.add(new Node(currentId, 3));

            currentId++;

            tmpNodeList.clear();

        }

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.println("id :" + node.id + " degree: " + node.degree);
        }

        edgeList.sort(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.src - o2.src;
            }
        });

        // 制造失效情况
//        makeFailure(edgeList, 0.4, totalNodeNumber);
//        makeFailure(edgeList, totalNodeNumber);

        File file = new File("/Users/Shawn/program/python/expander/edges.list");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
        for (Edge edge : edgeList) {
            bw.write(edge.src + "\t" + edge.target + "\n");
        }

        bw.close();


    }

    /**
     * 按比例选取失效点
     */
    static void makeFailure(List<Edge> edgeList, double removeRate, int totalNodeNumber) {

        // 增加失效情况，删除节点所在的所有边
        int removeNum = (int) (removeRate * totalNodeNumber);

        int removeOffset = 1;

        edgeList.sort(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.target - o2.target;
            }
        });

        Iterator<Edge> iteratorTarget = edgeList.iterator();
        while (iteratorTarget.hasNext() && removeOffset <= removeNum) {
            Edge edge = iteratorTarget.next();
            if (edge.target == removeOffset) {
                iteratorTarget.remove();
            } else {
                if (edge.target == removeOffset + 1 && removeOffset + 1 <= removeNum) {
                    iteratorTarget.remove();
                }
                removeOffset++;
            }

        }

        edgeList.sort(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.src - o2.src;
            }
        });

        removeOffset = 1;

        Iterator<Edge> iteratorSrc = edgeList.iterator();
        while (iteratorSrc.hasNext() && removeOffset <= removeNum) {
            Edge edge = iteratorSrc.next();
            if (edge.src == removeOffset) {
                iteratorSrc.remove();
            } else {
                if (edge.src == removeOffset + 1 && removeOffset + 1 <= removeNum) {
                    iteratorSrc.remove();
                }
                removeOffset++;
            }

        }


    }

    /**
     * 选取特定失效点
     *
     * @param edgeList
     * @param totalNodeNumber
     */
    static void makeFailure(List<Edge> edgeList, int totalNodeNumber) {

        // 从1~totalNodeNumber中选出removeNum个节点

        int[] removeIdList = {4, 5, 6, 7, 9, 13, 16};

        for (int id : removeIdList) {
            edgeList.removeIf(edge -> edge.src == id || edge.target == id);
        }

    }

    static class Node {
        int id;
        int degree;

        Node(int id, int degree) {
            this.id = id;
            this.degree = degree;
        }
    }

    static class Edge {
        int src;
        int target;

        public Edge(int src, int target) {
            this.src = src;
            this.target = target;
        }
    }
}
