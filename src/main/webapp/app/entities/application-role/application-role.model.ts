export interface IApplicationRole {
  id?: number;
  name?: string | null;
}

export class ApplicationRole implements IApplicationRole {
  constructor(public id?: number, public name?: string | null) {}
}

export function getApplicationRoleIdentifier(applicationRole: IApplicationRole): number | undefined {
  return applicationRole.id;
}
