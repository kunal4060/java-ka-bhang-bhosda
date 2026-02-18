/**
 * Problem 5: Advanced Stream Processing Pipeline with Filtering, Grouping, and Aggregation
 * Create a complex data processing pipeline that mimics real-world big data processing scenarios.
 * The pipeline should support multiple concurrent operations on streams of data.
 */
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Problem5_StreamProcessingPipeline {
    
    static class DataRecord {
        private String id;
        private String category;
        private double value;
        private long timestamp;
        private Map<String, Object> metadata;
        
        public DataRecord(String id, String category, double value, long timestamp) {
            this.id = id;
            this.category = category;
            this.value = value;
            this.timestamp = timestamp;
            this.metadata = new HashMap<>();
        }
        
        public DataRecord withMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }
        
        // Getters
        public String getId() { return id; }
        public String getCategory() { return category; }
        public double getValue() { return value; }
        public long getTimestamp() { return timestamp; }
        public Map<String, Object> getMetadata() { return metadata; }
        
        @Override
        public String toString() {
            return String.format("DataRecord{id='%s', category='%s', value=%.2f, timestamp=%d}", 
                               id, category, value, timestamp);
        }
    }
    
    static class ProcessingResult<T> {
        private final List<T> results;
        private final Map<String, Object> statistics;
        
        public ProcessingResult(List<T> results, Map<String, Object> statistics) {
            this.results = results;
            this.statistics = statistics;
        }
        
        public List<T> getResults() { return results; }
        public Map<String, Object> getStatistics() { return statistics; }
    }
    
    static class StreamProcessor<T> {
        private List<T> data;
        private Map<String, Object> stats;
        
        public StreamProcessor(List<T> data) {
            this.data = new ArrayList<>(data);
            this.stats = new ConcurrentHashMap<>();
        }
        
        public <R> StreamProcessor<R> map(Function<T, R> mapper) {
            List<R> mappedData = data.stream().map(mapper).collect(Collectors.toList());
            StreamProcessor<R> newProcessor = new StreamProcessor<>(mappedData);
            newProcessor.stats.putAll(this.stats); // Carry forward existing stats
            return newProcessor;
        }
        
        public StreamProcessor<T> filter(Predicate<T> predicate) {
            List<T> filteredData = data.stream().filter(predicate).collect(Collectors.toList());
            StreamProcessor<T> newProcessor = new StreamProcessor<>(filteredData);
            newProcessor.stats.putAll(this.stats); // Carry forward existing stats
            return newProcessor;
        }
        
        public <K> Map<K, List<T>> groupBy(Function<T, K> classifier) {
            return data.stream().collect(Collectors.groupingBy(classifier));
        }
        
        public <K> Map<K, Long> groupAndCount(Function<T, K> classifier) {
            return data.stream().collect(Collectors.groupingBy(classifier, Collectors.counting()));
        }
        
        public <K> Map<K, Double> groupAndAverage(Function<T, K> classifier, Function<T, Double> valueExtractor) {
            return data.stream().collect(Collectors.groupingBy(
                classifier, 
                Collectors.averagingDouble(valueExtractor::apply)
            ));
        }
        
        public T reduce(T identity, java.util.function.BinaryOperator<T> accumulator) {
            return data.stream().reduce(identity, accumulator);
        }
        
        public double average(Function<T, Double> valueExtractor) {
            return data.stream()
                      .mapToDouble(valueExtractor::apply)
                      .average()
                      .orElse(0.0);
        }
        
        public StreamProcessor<T> sort(Comparator<T> comparator) {
            List<T> sortedData = data.stream().sorted(comparator).collect(Collectors.toList());
            StreamProcessor<T> newProcessor = new StreamProcessor<>(sortedData);
            newProcessor.stats.putAll(this.stats);
            return newProcessor;
        }
        
        public ProcessingResult<T> collect() {
            // Calculate statistics
            stats.put("totalCount", data.size());
            stats.put("isEmpty", data.isEmpty());
            
            if (!data.isEmpty()) {
                stats.put("firstElement", data.get(0));
                stats.put("lastElement", data.get(data.size() - 1));
            }
            
            return new ProcessingResult<>(data, stats);
        }
    }
    
    public static void main(String[] args) {
        // Create sample data
        List<DataRecord> records = Arrays.asList(
            new DataRecord("R1", "A", 100.0, System.currentTimeMillis() - 3600000).withMetadata("source", "API"),
            new DataRecord("R2", "B", 150.0, System.currentTimeMillis() - 2700000).withMetadata("source", "FILE"),
            new DataRecord("R3", "A", 200.0, System.currentTimeMillis() - 1800000).withMetadata("source", "API"),
            new DataRecord("R4", "C", 75.0, System.currentTimeMillis() - 900000).withMetadata("source", "STREAM"),
            new DataRecord("R5", "B", 300.0, System.currentTimeMillis()).withMetadata("source", "API"),
            new DataRecord("R6", "A", 125.0, System.currentTimeMillis() - 4500000).withMetadata("source", "FILE"),
            new DataRecord("R7", "C", 225.0, System.currentTimeMillis() - 3600000).withMetadata("source", "STREAM"),
            new DataRecord("R8", "B", 175.0, System.currentTimeMillis() - 2700000).withMetadata("source", "API")
        );
        
        System.out.println("Original Records:");
        records.forEach(System.out::println);
        
        // Create a complex processing pipeline
        StreamProcessor<DataRecord> processor = new StreamProcessor<>(records);
        
        // Filter records with value > 100, group by category, and calculate average value
        System.out.println("\n--- Processing Pipeline ---");
        
        // Step 1: Filter records with value greater than 100
        StreamProcessor<DataRecord> filteredProcessor = processor.filter(record -> record.getValue() > 100);
        
        // Step 2: Group by category and calculate averages
        Map<String, Double> avgValuesByCategory = filteredProcessor.groupAndAverage(
            DataRecord::getCategory, 
            DataRecord::getValue
        );
        
        System.out.println("\nAverage values by category (after filtering > 100):");
        avgValuesByCategory.forEach((cat, avg) -> 
            System.out.println("Category " + cat + ": " + avg));
        
        // Step 3: Count records by category
        Map<String, Long> countByCategory = filteredProcessor.groupAndCount(DataRecord::getCategory);
        System.out.println("\nCounts by category (after filtering > 100):");
        countByCategory.forEach((cat, count) -> 
            System.out.println("Category " + cat + ": " + count));
        
        // Step 4: More complex pipeline - filter, sort, and collect
        ProcessingResult<DataRecord> result = processor
            .filter(record -> record.getValue() > 120)  // Filter by value
            .sort(Comparator.comparingDouble(DataRecord::getValue).reversed())  // Sort by value descending
            .collect();  // Collect results with statistics
        
        System.out.println("\nFiltered and sorted records (value > 120):");
        result.getResults().forEach(System.out::println);
        
        System.out.println("\nProcessing Statistics:");
        result.getStatistics().forEach((key, value) -> 
            System.out.println(key + ": " + value));
        
        // Parallel processing example
        System.out.println("\n--- Parallel Processing Example ---");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        Future<Map<String, Long>> futureCount = executor.submit(() -> {
            // Simulate heavy computation
            return processor.groupAndCount(DataRecord::getCategory);
        });
        
        Future<Double> futureAvg = executor.submit(() -> {
            // Simulate heavy computation
            return processor.average(DataRecord::getValue);
        });
        
        try {
            System.out.println("Category counts (parallel): " + futureCount.get());
            System.out.println("Overall average value (parallel): " + futureAvg.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}