//
// Created by Ravil Galiev on 28.02.2023.
//

#ifndef SETBENCH_DISTRIBUTION_BUILDER_H
#define SETBENCH_DISTRIBUTION_BUILDER_H

#include "common.h"

#include "parse_argument.h"

struct DistributionBuilder {
    DistributionType distributionType;
    DistributionParameters *parameters = nullptr;

    DistributionBuilder() : distributionType(DistributionType::UNIFORM) {}

    DistributionBuilder(DistributionType _distributionType) : distributionType(_distributionType) {}

    DistributionBuilder *setType(DistributionType _distributionType) {
        distributionType = _distributionType;
        return this;
    }

    DistributionBuilder *setParameters(DistributionParameters *const _parameters) {
        parameters = _parameters;
        return this;
    }

    bool parse(ParseArgument * args);

    Distribution *getDistribution(Random64 *rng, size_t range);

    MutableDistribution *getMutableDistribution(Random64 *rng);

    std::string toStringBuilderParameters() {
        return parameters != nullptr ? parameters->toString() : "";
    }

    ~DistributionBuilder() {
        delete parameters;
    }

};
#include "distributions/distribution_parameters_impls.h"

bool DistributionBuilder::parse(ParseArgument * args) {
    if (strcmp(args->getCurrent(), "-dist-zipf") == 0) {
        setType(DistributionType::ZIPF);
        setParameters(new ZipfParameters(atof(args->getNext())));
    } else if (strcmp(args->getCurrent(), "-dist-skewed-sets") == 0) {
        setType(DistributionType::SKEWED_SETS);
        //todo add parameters parse
    } else if (strcmp(args->getCurrent(), "-dist-uniform") == 0) {
        setType(DistributionType::UNIFORM);
    } else {
        return false;
    }
    return true;
}

Distribution *DistributionBuilder::getDistribution(Random64 *rng, size_t range) {
    switch (this->distributionType) {
        case DistributionType::UNIFORM:
            return new UniformDistribution(rng, range);
        case DistributionType::ZIPF:
            return new ZipfDistribution(rng, ((ZipfParameters *) parameters)->alpha, range);
        case DistributionType::SKEWED_SETS:
            SkewedSetParameters *skewedSetParameters = (SkewedSetParameters *) parameters;
            return new SkewedSetsDistribution(
                    skewedSetParameters->hotDistBuilder->
                            getDistribution(rng, skewedSetParameters->getHotLength(range)),
                    skewedSetParameters->coldDistBuilder->
                            getDistribution(rng, skewedSetParameters->getColdLength(range)),
                    rng,
                    skewedSetParameters->hotProb,
                    skewedSetParameters->getHotLength(range)
            );
    }
}

MutableDistribution *DistributionBuilder::getMutableDistribution(Random64 *rng) {
    switch (this->distributionType) {
        case DistributionType::UNIFORM:
            return new UniformDistribution(rng);
        case DistributionType::ZIPF:
            return new ZipfDistribution(rng, ((ZipfParameters *) parameters)->alpha);
        default:
            return NULL;
    }
}

#endif //SETBENCH_DISTRIBUTION_BUILDER_H
