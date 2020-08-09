//
// Created by dhruv on 08/08/20.
//

#include "TypeResolverImpl.h"

namespace hades {

TypeResolverImpl::TypeResolverImpl(core::Context* ctx) noexcept : m_ctx{ctx} {}

auto TypeResolverImpl::resolve_type_var(const type::Var& t) -> TypeResolutionResult {
  auto& source_file = ctx().get_source_file(*t.name().location().path());
  unimplemented();
}
auto TypeResolverImpl::ctx() -> core::Context & { return *m_ctx; }

} // namespace hades
