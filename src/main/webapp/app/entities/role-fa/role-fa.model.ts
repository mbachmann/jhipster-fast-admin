export interface IRoleFa {
  id?: number;
  name?: string | null;
}

export class RoleFa implements IRoleFa {
  constructor(public id?: number, public name?: string | null) {}
}

export function getRoleFaIdentifier(role: IRoleFa): number | undefined {
  return role.id;
}
