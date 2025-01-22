#ifndef SETBENCH_FILE_KEYS_DATA_MAP_BUILDER_H
#define SETBENCH_FILE_KEYS_DATA_MAP_BUILDER_H

#include <vector>
#include "workloads/data_maps/builders/array_data_map_builder.h"
#include "workloads/data_maps/data_map_builder.h"
#include "workloads/data_maps/impls/id_data_map.h"
#include "workloads/data_maps/impls/array_data_map.h"

class FileArrayDataMapBuilder : public ArrayDataMapBuilder {
    private:
        long long* data;

    FileArrayDataMapBuilder* init(size_t range) override {
        delete[] data;

        data = load_data("test-binary-file");

        //        std::random_shuffle(data, data + range - 1);
        std::shuffle(data, data + range, std::mt19937(std::random_device()()));
        return this;
    }

    void toJson(nlohmann::json& j) const override {
        j["ClassName"] = "FileArrayDataMapBuilder";
    }

    std::string toString(size_t indents = 1) override {
        return indented_title_with_str_data("Type", "FileArrayDataMap", indents) +
               indented_title_with_data("ID", id, indents);
    }
};

template <typename T>
std::vector<T> load_data(const std::string& filename,
                                bool print = true) {
  std::vector<T> data;
  const uint64_t ns = util::timing([&] {
    std::ifstream in(filename, std::ios::binary);
    if (!in.is_open()) {
      std::cerr << "unable to open " << filename << std::endl;
      exit(EXIT_FAILURE);
    }
    // Read size.
    uint64_t size;
    in.read(reinterpret_cast<char*>(&size), sizeof(uint64_t));
    data.resize(size);
    // Read values.
    in.read(reinterpret_cast<char*>(data.data()), size * sizeof(T));
    in.close();
  });
  const uint64_t ms = ns / 1e6;

  if (print) {
    std::cout << "read " << data.size() << " values from " << filename << " in "
              << ms << " ms (" << static_cast<double>(data.size()) / 1000 / ms
              << " M values/s)" << std::endl;
  }

  return data;
}

#endif  // SETBENCH_ARRAY_DATA_MAP_BUILDER_H