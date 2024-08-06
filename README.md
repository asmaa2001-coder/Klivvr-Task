# **Overview**
This project is a task from Klivver Company to
create an application that displays detailed 
information about cities. When a city is tapped, 
the application navigates to the city’s location 
on Google Maps. The main goal of this project is to handle large datasets from JSON
and use efficient search algorithms to minimize the time needed for data retrieval.

# **Features**
- Display City Details: Show detailed information about various cities.
- Google Maps Integration: Navigate to the city’s location on Google Maps when tapped.
- Efficient Data Handling: Utilize effective algorithms to handle large datasets and reduce search time.

## Data Handling Technique

We use the **Singleton pattern** to efficiently load and store the city data once. This avoids reloading and parsing the large JSON file every time we perform a search, which improves performance.

We use the `suspend` keyword for the `findCity` function to handle search operations asynchronously. This ensures that the search is performed in the background, keeping the app responsive.

Initially, we explored algorithms like **Trie nodes** and **binary search**. Tries are efficient for prefix searches, but they required reloading data frequently. Binary search was chosen for its efficiency with sorted data, and it's used after sorting the data.

In summary, the Singleton pattern reduces data loading time, and `suspend` functions ensure smooth, non-blocking search operations.
