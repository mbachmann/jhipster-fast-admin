export interface IRoleMySuffix {
  id?: number;
  name?: string | null;
}

export class RoleMySuffix implements IRoleMySuffix {
  constructor(public id?: number, public name?: string | null) {}
}

export function getRoleMySuffixIdentifier(role: IRoleMySuffix): number | undefined {
  return role.id;
}
