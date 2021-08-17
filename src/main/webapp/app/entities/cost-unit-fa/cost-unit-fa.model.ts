import { CostUnitType } from 'app/entities/enumerations/cost-unit-type.model';

export interface ICostUnitFa {
  id?: number;
  remoteId?: number | null;
  number?: string | null;
  name?: string | null;
  description?: string | null;
  type?: CostUnitType | null;
  inactiv?: boolean | null;
}

export class CostUnitFa implements ICostUnitFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public number?: string | null,
    public name?: string | null,
    public description?: string | null,
    public type?: CostUnitType | null,
    public inactiv?: boolean | null
  ) {
    this.inactiv = this.inactiv ?? false;
  }
}

export function getCostUnitFaIdentifier(costUnit: ICostUnitFa): number | undefined {
  return costUnit.id;
}
