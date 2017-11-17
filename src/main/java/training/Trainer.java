package training;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import training.btree.task.IsCloseEnough;
import training.btree.task.IsApproaching;
import training.btree.task.Move;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.branch.Selector;
import data.ProcessedData;
import training.btree.task.Wait;
import util.Grapher;
import util.Reader;

import java.util.List;

import static util.Values.*;

public class Trainer {

    private ProcessedData exampleData, iterationData;
    private double fitness;

    private void run() {
        Reader exampleDataReader = new Reader(exampleDataFilePath);
        Reader iterationDataReader = new Reader(iterationDataFilePath);

        List<String> exampleDataLine = exampleDataReader.readLine();
        List<String> iterationDataLine = iterationDataReader.readLine();

        while (exampleDataLine != null && iterationDataLine != null) {
            ProcessedData exampleData = new ProcessedData(exampleDataLine);
            ProcessedData iterationData = new ProcessedData(iterationDataLine);

            fitness += distanceFitness(exampleData.getDistance(), iterationData.getDistance());

            exampleDataLine = exampleDataReader.readLine();
            iterationDataLine = iterationDataReader.readLine();
            System.out.println(fitness);
        }
    }

    private double distanceFitness(double exampleDistance, double iterationDistance) {
        return Math.pow(Math.abs(exampleDistance - iterationDistance), 2);
    }

    private BehaviorTree cloneBehaviorTree(BehaviorTree btree) {
        Task root = btree.getChild(0);
        Task newRoot =  cloneSubtreeAndInsertChild(root, null, null, 0);
        return new BehaviorTree(newRoot);
    }

    private BehaviorTree cloneBehaviorTreeAndInsertChild(BehaviorTree btree, Task insertParent, Task insertChild, int insertIndex) {
        Task root = btree.getChild(0);
        Task newRoot =  cloneSubtreeAndInsertChild(root, insertParent, insertChild, insertIndex);
        return new BehaviorTree(newRoot);
    }

    private Task cloneSubtreeAndInsertChild(Task btree, Task insertParent, Task insertChild, int insertIndex) {
        Task newBtree = instantiateTaskObject(btree);
        if (insertIndex < 0 || (btree == insertParent && insertIndex > btree.getChildCount())) {
            throw new IllegalArgumentException("Invalid insertion index: " + insertIndex);
        }
        for (int i = 0; i < btree.getChildCount(); i++) {
            if (btree == insertParent && i == insertIndex) {
                newBtree.addChild(insertChild);
            }
            Task child = cloneSubtreeAndInsertChild(btree.getChild(i), insertParent, insertChild, insertIndex);
            newBtree.addChild(child);
        }
        if (btree == insertParent && btree.getChildCount() == insertIndex) {
            newBtree.addChild(insertChild);
        }
        return newBtree;
    }

    private Task instantiateTaskObject(Task task) {
        Task newTask = null;
        try {
            newTask = task.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return newTask;
    }

    public static void main(String[] args) {
        Trainer trainer = new Trainer();
        trainer.behaviorTreeTest();
    }

    private void behaviorTreeTest() {
        Selector selector1 = new Selector(new IsApproaching(), new IsCloseEnough());
        Sequence sequence1 = new Sequence(selector1, new Wait());
        Sequence sequence2 = new Sequence(sequence1, new Move());
        BehaviorTree btree = new BehaviorTree(sequence2);
        Grapher grapher = new Grapher("Original");
        grapher.graph(btree);

        Grapher grapher1 = new Grapher("Clone");
        grapher1.graph(cloneBehaviorTree(btree));

        Grapher grapher2 = new Grapher("Clone with insertion");
        grapher2.graph(cloneBehaviorTreeAndInsertChild(btree, sequence1, new Move(), 2));
    }

}