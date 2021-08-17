import { ICatalogServiceFa } from 'app/entities/catalog-service-fa/catalog-service-fa.model';

export interface IActivityFa {
  id?: number;
  remoteId?: number | null;
  name?: string | null;
  useServicePrice?: boolean | null;
  inactiv?: boolean | null;
  activity?: ICatalogServiceFa | null;
}

export class ActivityFa implements IActivityFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public name?: string | null,
    public useServicePrice?: boolean | null,
    public inactiv?: boolean | null,
    public activity?: ICatalogServiceFa | null
  ) {
    this.useServicePrice = this.useServicePrice ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getActivityFaIdentifier(activity: IActivityFa): number | undefined {
  return activity.id;
}
