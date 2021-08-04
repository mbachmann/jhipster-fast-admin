import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILocationMySuffix, LocationMySuffix } from '../location-my-suffix.model';
import { LocationMySuffixService } from '../service/location-my-suffix.service';
import { IDepartmentMySuffix } from 'app/entities/department-my-suffix/department-my-suffix.model';
import { DepartmentMySuffixService } from 'app/entities/department-my-suffix/service/department-my-suffix.service';

@Component({
  selector: 'jhl-location-my-suffix-update',
  templateUrl: './location-my-suffix-update.component.html',
})
export class LocationMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  departmentsSharedCollection: IDepartmentMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    streetAddress: [],
    postalCode: [],
    city: [],
    stateProvince: [],
    department: [],
  });

  constructor(
    protected locationService: LocationMySuffixService,
    protected departmentService: DepartmentMySuffixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ location }) => {
      this.updateForm(location);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const location = this.createFromForm();
    if (location.id !== undefined) {
      this.subscribeToSaveResponse(this.locationService.update(location));
    } else {
      this.subscribeToSaveResponse(this.locationService.create(location));
    }
  }

  trackDepartmentMySuffixById(index: number, item: IDepartmentMySuffix): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocationMySuffix>>): void {
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

  protected updateForm(location: ILocationMySuffix): void {
    this.editForm.patchValue({
      id: location.id,
      streetAddress: location.streetAddress,
      postalCode: location.postalCode,
      city: location.city,
      stateProvince: location.stateProvince,
      department: location.department,
    });

    this.departmentsSharedCollection = this.departmentService.addDepartmentMySuffixToCollectionIfMissing(
      this.departmentsSharedCollection,
      location.department
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartmentMySuffix[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartmentMySuffix[]) =>
          this.departmentService.addDepartmentMySuffixToCollectionIfMissing(departments, this.editForm.get('department')!.value)
        )
      )
      .subscribe((departments: IDepartmentMySuffix[]) => (this.departmentsSharedCollection = departments));
  }

  protected createFromForm(): ILocationMySuffix {
    return {
      ...new LocationMySuffix(),
      id: this.editForm.get(['id'])!.value,
      streetAddress: this.editForm.get(['streetAddress'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
      city: this.editForm.get(['city'])!.value,
      stateProvince: this.editForm.get(['stateProvince'])!.value,
      department: this.editForm.get(['department'])!.value,
    };
  }
}
