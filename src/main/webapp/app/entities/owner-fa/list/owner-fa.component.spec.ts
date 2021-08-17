import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OwnerFaService } from '../service/owner-fa.service';

import { OwnerFaComponent } from './owner-fa.component';

describe('Component Tests', () => {
  describe('OwnerFa Management Component', () => {
    let comp: OwnerFaComponent;
    let fixture: ComponentFixture<OwnerFaComponent>;
    let service: OwnerFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OwnerFaComponent],
      })
        .overrideTemplate(OwnerFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OwnerFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OwnerFaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.owners?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
