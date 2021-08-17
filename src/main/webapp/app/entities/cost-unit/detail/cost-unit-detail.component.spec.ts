import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CostUnitDetailComponent } from './cost-unit-detail.component';

describe('Component Tests', () => {
  describe('CostUnit Management Detail Component', () => {
    let comp: CostUnitDetailComponent;
    let fixture: ComponentFixture<CostUnitDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CostUnitDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ costUnit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CostUnitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CostUnitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load costUnit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.costUnit).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
