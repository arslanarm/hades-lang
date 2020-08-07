#ifndef HADES_LOCATION_H
#define HADES_LOCATION_H
#include "hades/base/sys.h"
#include <cstdint>
#include <hades/base.h>

namespace hades {

using LineNumber = uint64_t;
using ColumnNumber = uint64_t;

struct SourcePosition {
private:
  LineNumber m_line;
  LineNumber m_column;

public:
  SourcePosition(LineNumber line, LineNumber column);
  LineNumber line();
  ColumnNumber column();
};

class SourceLocation {
  const fs::path *m_path;
  SourcePosition m_start;
  SourcePosition m_stop;

public:
  SourceLocation(const fs::path *path, SourcePosition start,
                 SourcePosition stop) noexcept;
  HADES_DEFAULT_COPY(SourceLocation);
  HADES_DEFAULT_MOVE(SourceLocation);
  auto path() const noexcept -> const fs::path *;
  auto start() const noexcept -> SourcePosition;
  auto stop() const noexcept -> SourcePosition;

  static auto between(const fs::path *path, SourceLocation start,
                      SourceLocation stop) noexcept -> SourceLocation;

  auto location() const -> const SourceLocation &;
};
static_assert(std::is_trivially_copyable_v<SourceLocation>);
static_assert(std::is_trivially_move_assignable_v<SourceLocation>);

} // namespace hades
#endif