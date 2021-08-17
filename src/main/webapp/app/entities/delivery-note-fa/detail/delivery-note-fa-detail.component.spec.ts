import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliveryNoteFaDetailComponent } from './delivery-note-fa-detail.component';

describe('Component Tests', () => {
  describe('DeliveryNoteFa Management Detail Component', () => {
    let comp: DeliveryNoteFaDetailComponent;
    let fixture: ComponentFixture<DeliveryNoteFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DeliveryNoteFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ deliveryNote: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DeliveryNoteFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeliveryNoteFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load deliveryNote on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.deliveryNote).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
