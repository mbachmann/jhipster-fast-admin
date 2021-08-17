jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EffortFaService } from '../service/effort-fa.service';

import { EffortFaDeleteDialogComponent } from './effort-fa-delete-dialog.component';

describe('Component Tests', () => {
  describe('EffortFa Management Delete Component', () => {
    let comp: EffortFaDeleteDialogComponent;
    let fixture: ComponentFixture<EffortFaDeleteDialogComponent>;
    let service: EffortFaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EffortFaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(EffortFaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EffortFaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EffortFaService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
