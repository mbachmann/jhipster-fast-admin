import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IActivityFa, ActivityFa } from '../activity-fa.model';
import { ActivityFaService } from '../service/activity-fa.service';
import { ICatalogServiceFa } from 'app/entities/catalog-service-fa/catalog-service-fa.model';
import { CatalogServiceFaService } from 'app/entities/catalog-service-fa/service/catalog-service-fa.service';

@Component({
  selector: 'fa-activity-fa-update',
  templateUrl: './activity-fa-update.component.html',
})
export class ActivityFaUpdateComponent implements OnInit {
  isSaving = false;

  catalogServicesSharedCollection: ICatalogServiceFa[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    name: [],
    useServicePrice: [],
    inactiv: [],
    activity: [],
  });

  constructor(
    protected activityService: ActivityFaService,
    protected catalogServiceService: CatalogServiceFaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activity }) => {
      this.updateForm(activity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activity = this.createFromForm();
    if (activity.id !== undefined) {
      this.subscribeToSaveResponse(this.activityService.update(activity));
    } else {
      this.subscribeToSaveResponse(this.activityService.create(activity));
    }
  }

  trackCatalogServiceFaById(index: number, item: ICatalogServiceFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivityFa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(activity: IActivityFa): void {
    this.editForm.patchValue({
      id: activity.id,
      remoteId: activity.remoteId,
      name: activity.name,
      useServicePrice: activity.useServicePrice,
      inactiv: activity.inactiv,
      activity: activity.activity,
    });

    this.catalogServicesSharedCollection = this.catalogServiceService.addCatalogServiceFaToCollectionIfMissing(
      this.catalogServicesSharedCollection,
      activity.activity
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogServiceService
      .query()
      .pipe(map((res: HttpResponse<ICatalogServiceFa[]>) => res.body ?? []))
      .pipe(
        map((catalogServices: ICatalogServiceFa[]) =>
          this.catalogServiceService.addCatalogServiceFaToCollectionIfMissing(catalogServices, this.editForm.get('activity')!.value)
        )
      )
      .subscribe((catalogServices: ICatalogServiceFa[]) => (this.catalogServicesSharedCollection = catalogServices));
  }

  protected createFromForm(): IActivityFa {
    return {
      ...new ActivityFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      name: this.editForm.get(['name'])!.value,
      useServicePrice: this.editForm.get(['useServicePrice'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
      activity: this.editForm.get(['activity'])!.value,
    };
  }
}
