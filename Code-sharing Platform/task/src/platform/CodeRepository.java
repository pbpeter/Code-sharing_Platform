package platform;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {

    List<Code> findTop10ByTimeLessThanEqualAndViewsRestrictedFalseOrderByIdDesc(long time);

    Optional<Code> findByUuid(String uuid);
}