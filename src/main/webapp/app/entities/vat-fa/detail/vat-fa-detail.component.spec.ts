import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VatFaDetailComponent } from './vat-fa-detail.component';

describe('Component Tests', () => {
  describe('VatFa Management Detail Component', () => {
    let comp: VatFaDetailComponent;
    let fixture: ComponentFixture<VatFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VatFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ vat: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VatFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VatFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vat on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vat).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
