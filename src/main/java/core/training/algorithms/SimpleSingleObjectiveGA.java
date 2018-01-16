package core.training.algorithms;


import core.data.ExampleDataSet;
import core.data.rows.DataRow;
import core.model.btree.EvaluatedGenBehaviorTree;
import core.settings.algorithms.SimpleSingleObjectiveGASettings;
import core.simulation.SimController;
import core.training.Population;
import core.training.Trainer;
import core.util.SystemStatus;
import core.util.SystemUtil;

import java.util.Comparator;
import java.util.Random;

public class SimpleSingleObjectiveGA extends Algorithm {

    OneDimensionalComparator oneDimensionalComparator;
    Random random;

    public SimpleSingleObjectiveGA(){};

    public SimpleSingleObjectiveGA(Trainer trainer) {
        super(trainer);
    }

    @Override
    public void setup() {
        oneDimensionalComparator = new OneDimensionalComparator();
        random = new Random();
        population = new Population();
        population.generateRandomPopulation(SimpleSingleObjectiveGASettings.populationSize);
    }

    @Override
    public void step(int epoch, int exampleNumber, ExampleDataSet<? extends DataRow> exampleDataSet) {
        // SIMULATION
            trainer.simulatePopulation(population);


        // EVALUATION

        // DONE Obtain example file
        // Obtain files for simulated population
        for (int chromosome = 0; chromosome < population.getSize(); chromosome++) {
            String intraResourcesScenarioLogsPath = SystemUtil.getDataFileIntraResourcesFolderPath(epoch, exampleNumber, chromosome);

        }

            // Evaluate each chromosome by running functions in FitnessFunctions and store in fitness list in the chromosomes

        // SELECTION
            population.sort(oneDimensionalComparator);
            Population newPopulation = new Population();

            // Elitism
            for (int i = 0; i < SimpleSingleObjectiveGASettings.elitismPercentage * population.getSize(); i++) {
                newPopulation.add(population.get(i));
            }
            // TODO Replace random with binary tournament
            // Crossover or mutation
            while (newPopulation.getSize() > SimpleSingleObjectiveGASettings.populationSize) {
                int randIndex1 = random.nextInt(population.getSize());
                int randIndex2 = random.nextInt(population.getSize());
                int parent1index = Math.min(randIndex1, randIndex2);

                if (random.nextDouble() < SimpleSingleObjectiveGASettings.crossoverRate) {
                    int randIndex3 = random.nextInt(population.getSize());
                    int randIndex4 = random.nextInt(population.getSize());
                    int parent2index = Math.min(randIndex3, randIndex4);
                    newPopulation.add(population.crossover(parent1index, parent2index));
                } else if (random.nextDouble() < SimpleSingleObjectiveGASettings.mutationRate) {
                    newPopulation.add(population.mutate(parent1index));
                } else {
                    newPopulation.add(population.cloneElement(parent1index));
                }
            }

        population = newPopulation;
    }


    @Override
    public void cleanup() {

    }

    private class OneDimensionalComparator implements Comparator<EvaluatedGenBehaviorTree> {
        @Override
        public int compare(EvaluatedGenBehaviorTree o1, EvaluatedGenBehaviorTree o2) {
            if (o1.getFitness().get(0) < o2.getFitness().get(0)) {
                return 1;
            } else if (o1.getFitness().get(0) > o2.getFitness().get(0)) {
                return -1;
            }
            return 0;
        }
    }
}
