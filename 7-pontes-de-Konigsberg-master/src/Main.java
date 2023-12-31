import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class Main
{
    private Node currentNode;
    private List<Node> nodes;
    private List<Route> remainingRoutes;
    private List<String> solution = new ArrayList<>();
    private Stack<State> states = new Stack<>(); 

    public static void main(String[] args)
    {
        new Main().run(); 
    }

    private void run()
    {
        String fileName = "input.txt";
        nodes = MyReader.readNodesFromFile(fileName);
        remainingRoutes = MyReader.createRoutesAndConnectTheNodes(fileName, nodes);

        
        int oddNums = 0;
        for(Node n : nodes){
            if(n.getAvailableRoutes().size() % 2 != 0){
                oddNums++;
            }
        }

        if(oddNums!=0 && oddNums!=2){
            System.out.println("Não há solução para este gráfico (o caminho de Euler não pode ser encontrado).");
        }else {
            currentNode = nodes.get(0);
            do {
                if (currentNode.getAvailableRoutes().size() != 0) {
                    states.push(new State(currentNode, nodes, remainingRoutes));

                    Route candidateRoute = currentNode.getAvailableRoutes().get(0);
                    remainingRoutes.remove(candidateRoute);
                    addToSolution(candidateRoute, currentNode);
                    currentNode = currentNode.crossBetweenNodes(candidateRoute, nodes);
                } else {
                    previousState();
                }
            } while (remainingRoutes.size() != 0);

            printSolution();
        }
    }

    private void previousState()
    {
        if(states.empty()) return;

        try {
            solution.remove(solution.size() - 1);

            State s = states.pop();
            Node sNode = s.getCurrentNode();
            currentNode = (Node) sNode.clone();

            this.remainingRoutes.clear();
            this.nodes.clear();
            for (Node n : s.getNodes()) {
                if (n != null) {
                    this.nodes.add((Node) n.clone());
                }
            }
            for (Route r : s.getRoutes()) {
                if (r != null) {
                    this.remainingRoutes.add((Route) r.clone());
                }
            }
        }catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        for(Node n : this.nodes)
        {
            if(n.equals(currentNode)){
                currentNode = n;
            }
        }
        
        Route r = currentNode.getAvailableRoutes().get(0);
        currentNode.getAvailableRoutes().remove(r);
        currentNode.getAvailableRoutes().add(r);
    }

    private void addToSolution(final Route route, final Node currentNode)
    {
        char curNodeName = currentNode.getName();
        if(route.getNode1().getName() == currentNode.getName()){
            solution.add("From Node "+curNodeName+" to Node "+route.getNode2().getName());
        }else{
            solution.add("From Node "+curNodeName+" to Node "+route.getNode1().getName());
        }
    }

    private void printSolution()
    {
        System.out.println("it has a solution!\n");
        for(String sol : solution){
            System.out.println(sol);
        }
    }

}
