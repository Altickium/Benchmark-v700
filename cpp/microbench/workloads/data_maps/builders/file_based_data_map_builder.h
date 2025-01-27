#ifndef SETBENCH_FILE_BASED_DATA_MAP_BUILDER_H
#define SETBENCH_FILE_BASED_DATA_MAP_BUILDER_H

#include "workloads/data_maps/builders/array_data_map_builder.h"
#include "workloads/data_maps/data_map_builder.h"
#include "workloads/data_maps/impls/id_data_map.h"
#include "workloads/data_maps/impls/array_data_map.h"
#include <iostream>
#include <fstream>
#include <vector>

class FileArrayDataMapBuilder : protected ArrayDataMapBuilder {
  private:
    std::string filename = "test-binary-file";

  public:
    FileArrayDataMapBuilder() {
      filename = "test-binary-file";
    }

    FileArrayDataMapBuilder(FileArrayDataMapBuilder& other) {
      filename = other.filename;
    }

    FileArrayDataMapBuilder& operator= (FileArrayDataMapBuilder& other) {
      filename = other.filename;
      return *this;
    }

    FileArrayDataMapBuilder* init(size_t range) override {
        delete[] data;

        data = &load_file_data(filename)[0];

        //        std::random_shuffle(data, data + range - 1);
        std::shuffle(data, data + range, std::mt19937(std::random_device()()));
        return this;
    }

    void toJson(nlohmann::json& j) const override {
        j["ClassName"] = "FileArrayDataMapBuilder";
    }

    std::string toString(size_t indents = 1) override {
        return indented_title_with_str_data("Type", "FileBasedArrayDataMap", indents) +
               indented_title_with_data("ID", id, indents);
    }

    FileArrayDataMapBuilder* setFilename(std::string filename) {
      this -> filename = filename;
      return this;
    }

    ~FileArrayDataMapBuilder() override = default;
};

std::vector<long long> load_file_data(std::string& filename,
                                bool print = true) {
  std::vector<long long> data;
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
  in.read(reinterpret_cast<char*>(data.data()), size * sizeof(long long));
  in.close();

  if (print) {
    std::cout << "read " << data.size() << " values from " << filename << " in "
              << " M values/s)" << std::endl;
  }

  return data;
}

#endif  // SETBENCH_FILE_BASED_DATA_MAP_BUILDER_H