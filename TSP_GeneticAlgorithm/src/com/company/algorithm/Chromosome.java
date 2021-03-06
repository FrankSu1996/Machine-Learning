package com.company.algorithm;

import java.util.*;

/**
 * Class to represent a chromosome in the genetic algorithm. For TSP, represents a possible
 * path that connects all cities.
 */
public class Chromosome {

    private final List<Gene> chromosome;

    Chromosome(final List<Gene> chromosome) {
        this.chromosome = Collections.unmodifiableList(chromosome);
    }

    /**
     * Method to create initial chromosomes given a array of genes/points. Returns a single chromosome
     * built from a random list of genes after shuffling.
     * @param points an array of genes representing cities
     * @return a chromosome for initial population
     */
    static Chromosome create(final Gene[] points) {
        final List<Gene> genes = Arrays.asList(Arrays.copyOf(points, points.length));
        Collections.shuffle(genes);
        return new Chromosome(genes);
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(final Gene gene : this.chromosome) {
            builder.append(gene.toString()).append((" : "));
        }
        return builder.toString();
    }

    public List<Gene> getChromosome() {
        return this.chromosome;
    }

    /**
     * Method to return total distance of chromosome, represents it's fitness
     * @return the total distance of chromosome. Essentially a random solution to TSP
     */
    public double calculateDistance() {
        double total = 0.0f;
        for(int i = 0; i < this.chromosome.size() - 1; i++) {
            total += this.chromosome.get(i).distance(this.chromosome.get(i+1));
        }
        return total;
    }

    /**
     * Crossover method to produce children from two parent chromosomes
     * @param other Other chromosome to be crossed over with
     * @return an array of two children chromosomes
     */
    Chromosome[] crossOver(final Chromosome other) {

        //split chromosomes into two for both this and other chromosome
        final List<Gene>[] myDNA = TSPUtils.split(this.chromosome);
        final List<Gene>[] otherDNA = TSPUtils.split(other.getChromosome());

        //first crossover starts with first "half" of myDNA
        final List<Gene> firstCrossOver = new ArrayList<>(myDNA[0]);
        //loop through other DNA list, if not contained already, add to myDNA
        //this mimics random crossing over, as distance is calculated sequentially through the dna list
        for(Gene gene : otherDNA[0]) {
            if(!firstCrossOver.contains(gene)) {
                firstCrossOver.add(gene);
            }
        }
        for(Gene gene : otherDNA[1]) {
            if(!firstCrossOver.contains(gene)) {
                firstCrossOver.add(gene);
            }
        }

        //repeat process for a second crossover
        final List<Gene> secondCrossOver = new ArrayList<>(otherDNA[1]);
        for(Gene gene : myDNA[0]) {
            if(!secondCrossOver.contains(gene)) {
                secondCrossOver.add(gene);
            }
        }

        for(Gene gene : myDNA[1]) {
            if(!secondCrossOver.contains(gene)) {
                secondCrossOver.add(gene);
            }
        }

        return new Chromosome[] {new Chromosome(firstCrossOver), new Chromosome(secondCrossOver)};
    }

    /**
     * Method to mutate a given chromosome. Switches two random indexes
     * @return a copy of the mutated chromosome.
     */
    Chromosome mutate() {
        final List<Gene> copy = new ArrayList<>(this.chromosome);

        for (int i = 0; i < TSPUtils.MUTATION_SEVERITY; i++) {
            //get two random indexes for chromosome, make sure they aren't the same
            int indexA = TSPUtils.randomIndex(copy.size());
            int indexB = TSPUtils.randomIndex(copy.size());
            while (indexA == indexB) {
                indexA = TSPUtils.randomIndex(copy.size());
                indexB = TSPUtils.randomIndex(copy.size());
            }
            //swap indexes. Imitates random "mutation" in the chromosome
            Collections.swap(copy, indexA, indexB);
        }
        return new Chromosome(copy);
    }
}
